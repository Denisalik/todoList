import { api } from './api.ts';
import { useStore } from './store.ts';
import { type Actions, AllActionsEnum, type AuthActions, type AuthMutations } from './types.ts';

//using interceptors(./api.ts) if status is not 200/201
export const authContextActions: AuthMutations = {
  login: async (username: string, password: string) => {
    useStore.getState().dispatch({
      type: AllActionsEnum.AUTH_LOADING,
      payload: true,
    });
    const { data, status } = await api.login({ username, password });
    if (status === 201) {
      const payload = {
        token: data.accessToken,
        error: undefined,
        isAuthenticated: true,
        isLoading: false,
      };
      useStore.getState().dispatch({ type: AllActionsEnum.LOGIN, payload });
    }
  },
  register: async (username: string, email: string, password: string) => {
    useStore.getState().dispatch({
      type: AllActionsEnum.AUTH_LOADING,
      payload: true,
    });
    const { data, status } = await api.registration({ username, email, password });
    if (status === 201) {
      const payload = {
        token: data.accessToken,
        error: undefined,
        isAuthenticated: true,
        isLoading: false,
      };
      useStore.getState().dispatch({ type: AllActionsEnum.REGISTER, payload });
    }
  },
  refresh: async () => {
    const { data, status } = await api.refresh();
    if (status === 200) {
      const payload = {
        token: data.accessToken,
        error: undefined,
        isAuthenticated: true,
      };
      useStore.getState().dispatch({ type: AllActionsEnum.REFRESH, payload });
    }
  },
  me: async () => {
    useStore.getState().dispatch({
      type: AllActionsEnum.AUTH_LOADING,
      payload: true,
    });
    const { data, status } = await api.me();
    if (status === 200) {
      const payload = {
        user: data,
        error: undefined,
        isAuthenticated: true,
        isLoading: false,
      };
      useStore.getState().dispatch({ type: AllActionsEnum.ME, payload });
    }
  },
  logout: () => {
    const payload = {
      token: undefined,
      isAuthenticated: false,
      error: undefined,
      isLoading: false,
    };
    useStore.getState().dispatch({ type: AllActionsEnum.LOGOUT, payload });
  },
};
const AUTH_ACTIONS = [
  AllActionsEnum.LOGIN,
  AllActionsEnum.REGISTER,
  AllActionsEnum.LOGOUT,
  AllActionsEnum.ME,
  AllActionsEnum.REFRESH,
];

export const isAuthAction = (action: Actions): action is AuthActions =>
  (AUTH_ACTIONS as string[]).includes(action.type);
