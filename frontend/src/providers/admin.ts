import { api } from '@/providers/api.ts';
import { useStore } from '@/providers/store.ts';
import { type AdminMutations, AllActionsEnum } from '@/providers/types.ts';

export const adminContextActions: AdminMutations = {
  getUsers: async () => {
    useStore.getState().dispatch({
      type: AllActionsEnum.ADMIN_LOADING,
      payload: true,
    });
    const { data, status } = await api.getUsers();
    if (status === 200) {
      const payload = {
        users: data,
        error: undefined,
        isLoading: false,
      };
      useStore.getState().dispatch({ type: AllActionsEnum.GET_USERS, payload });
    }
  },
};
