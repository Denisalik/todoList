import {create} from "zustand";
import {devtools, redux, subscribeWithSelector} from "zustand/middleware";
import {type Actions, AllActionsEnum, type StoreState,} from "./types.ts";
import {isAuthAction} from "./auth.ts";
import {isToDoAction} from "@/providers/toDoList.ts";

const initialState: StoreState = {
  authentication: {
    isAuthenticated: false,
    error: undefined,
    token: undefined,
    user: undefined,
    isLoading: false,
  },
  toDoList: {
    list: [],
    error: undefined,
    isLoading: false,
    isLoadingItem: undefined,
  },
  admin: {
    users: [],
    isLoading: false,
    error: undefined,
  },
};

const reducer = (state: StoreState, action: Actions): StoreState => {
  if (isAuthAction(action)) {
    return {...state, authentication: {...state.authentication, ...action.payload}};
  }
  if (isToDoAction(action)) {
    const {list, ...others} = action.payload;
    const extendedList = [...state.toDoList.list]
      .filter(todo => !list.some(newToDo => newToDo.id === todo.id))
      .concat(list)
      .sort((a,b) => a.selected !== b.selected ? (a.selected ? -1 : 1) : a.id! - b.id!);
    return {...state, toDoList: {...state.toDoList, ...others, list: extendedList}};
  }
  switch (action.type) {
    case AllActionsEnum.AUTH_LOADING:
      return {...state, authentication: {...state.authentication, isLoading: action.payload}};
    case AllActionsEnum.TODO_LOADING:
      return {...state, toDoList: {...state.toDoList, isLoading: action.payload}};
    case AllActionsEnum.TODO_ID_LOADING:
      return {...state, toDoList: {...state.toDoList, isLoadingItem: action.payload}};
    case AllActionsEnum.ADMIN_LOADING:
      return {...state, admin: {...state.admin, isLoading: action.payload}}
    case AllActionsEnum.ALL_LOADING:
      return {...state, authentication: {...state.authentication, isLoading: action.payload}, toDoList: {...state.toDoList, isLoading: action.payload}}
    case AllActionsEnum.DELETE_TODO_ITEM:
      const list = state.toDoList.list.filter(todo => todo.id !== action.payload);
      return {...state, toDoList: {...state.toDoList, list, isLoading: false, error: undefined}};
    case AllActionsEnum.SUGGEST_TODO_ITEM:
      const {isLoadingItem, error, suggestion, id} = action.payload;
      const item = state.toDoList.list.find(todo => todo.id === id);
      const listWithSuggestion = state.toDoList.list
        .filter(todo => todo.id !== id)
        .concat([{...item, answer: suggestion}]);
      return {...state, toDoList: {...state.toDoList, error, isLoadingItem, list: listWithSuggestion}};
    case AllActionsEnum.GET_USERS:
      return {...state, admin: {...state.admin, ...action.payload}};
    default:
      return state
  }
};


export const useStore = create(devtools(subscribeWithSelector(redux(reducer, initialState))));