import type {GetMeResponse, ToDoItem, ToDoSuggestion, User} from "@/generated/model";

export interface IsLoadingItem {
  id: number;
}

export interface ToDoListState {
  list: ToDoItem[];
  isLoading: boolean;
  isLoadingItem?: IsLoadingItem;
  error?: string;
}

export interface AdminState {
  users: User[];
  isLoading: boolean;
  error?: string;
}
export interface AuthState {
  user?: GetMeResponse;
  token?: string;
  isAuthenticated: boolean;
  isLoading: boolean;
  error?: string;
}

export interface StoreState {
  authentication: AuthState;
  toDoList: ToDoListState;
  admin: AdminState;
}

export enum AllActionsEnum {
  LOGIN = "LOGIN",
  REGISTER = "REGISTER",
  LOGOUT = "LOGOUT",
  ME = "ME",
  REFRESH = "REFRESH",

  AUTH_LOADING = "AUTH_LOADING",
  TODO_LOADING = "TODO_LOADING",
  TODO_ID_LOADING = "TODO_ID_LOADING",
  ADMIN_LOADING = "ADMIN_LOADING",
  ALL_LOADING = "ALL_LOADING",

  GET_TODO_LIST = "GET_TODO_LIST",
  ADD_TODO_ITEM = "ADD_TODO_ITEM",
  UPDATE_TODO_ITEM = "UPDATE_TODO_ITEM",
  DELETE_TODO_ITEM = "DELETE_TODO_ITEM",
  SUGGEST_TODO_ITEM = "SUGGEST_TODO_ITEM",

  GET_USERS = "GET_USERS",
}
type OptionalAuthState = Partial<AuthState>;
type OptionalAdminState = Partial<AdminState>;

interface SuggestionPayload {
  suggestion: ToDoSuggestion;
  id: number;
  isLoadingItem?: IsLoadingItem;
  error: string | undefined;
}

export type AuthActions =
  | { type: AllActionsEnum.LOGIN; payload: OptionalAuthState }
  | { type: AllActionsEnum.REGISTER; payload: OptionalAuthState }
  | { type: AllActionsEnum.LOGOUT; payload: OptionalAuthState }
  | { type: AllActionsEnum.ME; payload: OptionalAuthState }
  | { type: AllActionsEnum.REFRESH; payload: OptionalAuthState};

export type ToDoActions =
  | { type: AllActionsEnum.GET_TODO_LIST; payload: ToDoListState }
  | { type: AllActionsEnum.ADD_TODO_ITEM; payload: ToDoListState }
  | { type: AllActionsEnum.UPDATE_TODO_ITEM; payload: ToDoListState };

export type LoadingActions =
  | { type: AllActionsEnum.AUTH_LOADING; payload: boolean}
  | { type: AllActionsEnum.TODO_LOADING; payload: boolean}
  | { type: AllActionsEnum.TODO_ID_LOADING; payload: IsLoadingItem}
  | { type: AllActionsEnum.ADMIN_LOADING; payload: boolean}
  | { type: AllActionsEnum.ALL_LOADING; payload: boolean};

export type Actions =
  | AuthActions
  | ToDoActions
  | LoadingActions
  | { type: AllActionsEnum.GET_USERS; payload: OptionalAdminState}
  | { type: AllActionsEnum.DELETE_TODO_ITEM; payload: number}
  | { type: AllActionsEnum.SUGGEST_TODO_ITEM; payload: SuggestionPayload };

export interface AuthMutations {
  login: (username: string, password: string) => Promise<void>;
  register: (username: string, email: string, password: string) => Promise<void>;
  logout: () => void;
  me: () => Promise<void>;
  refresh: () => Promise<void>;
}
export interface UpdateItemPayload {
  title?: string;
  description?: string;
  selected?: boolean;
}
export interface ToDoMutations {
  getList: () => Promise<void>;
  updateItem: (id: number, payload: UpdateItemPayload) => Promise<void>;
  deleteItem: (id: number) => void;
  addItem: (title: string, description: string) => Promise<void>;
  suggestItem: (id: number) => Promise<void>;
}
export interface AdminMutations {
  getUsers: () => Promise<void>;
}