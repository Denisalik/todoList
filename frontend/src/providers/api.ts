import {getToDoListAPI} from "@/generated/openapi.ts";
import {type AxiosError, create} from "axios";
import type {ErrorResponse} from "@/generated/model";
import {useStore} from "./store.ts";
import {useNavigate} from "@tanstack/react-router";
import {Route} from "@/routes/login.tsx";
import {authContextActions} from "./auth.ts";
import {AllActionsEnum} from "@/providers/types.ts";
import {showMessage} from "@/providers/message.ts";
import {getCookie, isNonNull} from "@/util.ts";

type ExceptionHandlerError = AxiosError<ErrorResponse>;
const baseURL = import.meta.env.VITE_BACKEND_API_BASE;
const myAxios = create({baseURL, withCredentials: true});

myAxios.interceptors.request.use(
  (config) => {
    const token = useStore.getState().authentication.token;
    const csrf = getCookie('XSRF-TOKEN');
    if (token && csrf) {
      config.headers.Authorization = `Bearer ${token}`;
      config.headers['X-XSRF-TOKEN'] = csrf;
    }
    return config;
  }
);
myAxios.interceptors.response.use(
  (response) => response,
  async (error: ExceptionHandlerError) => {
    if (error.response === undefined) {
      console.error('An unknown error occurred.');
      return;
    }
    const originalRequest = error.config as typeof error.config & { _retry?: boolean };
    const {message, errors, status} = error.response.data;
    console.error('Message error:', message);
    console.error('Status:', status);
    if (isNonNull(errors) && Object.keys(errors).length !== 0) console.error('Errors:', errors);

    if (status === 401) {
      // if it is 401 Unauthorized:
      // 1. Access token does not exist, then redirect to /login.
      // 2. Access token is expired or not working, and the refresh token exists, so refreshing access token:
      // 3.1. Refresh token is working and refresh access token
      // 3.2. Refresh token is not working, then retry is true and redirect to /login
      const {token} = useStore.getState().authentication;
      if (token && !originalRequest._retry) {
        originalRequest._retry = true;
        await authContextActions.refresh();
        return myAxios.request(originalRequest);
      }
      authContextActions.logout();
      const navigate = useNavigate();
      const {redirect} = Route.useSearch();
      await navigate({to: '/login', search: {redirect}});
      return Promise.reject(error);
    }
    useStore.getState().dispatch({
      type: AllActionsEnum.ALL_LOADING,
      payload: false,
    });
    const description = isNonNull(errors)
      ? message + '\n' + Object.entries(errors)
      .map(([key, value]) => key + ': ' + value)
      .join('\n')
      : message;

    showMessage.error(description);
    return Promise.reject(error);
  }
);
export const api = getToDoListAPI(myAxios);