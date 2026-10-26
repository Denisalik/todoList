import {
  type Actions,
  AllActionsEnum,
  type ToDoActions,
  type ToDoMutations,
  type UpdateItemPayload
} from "@/providers/types.ts";
import {useStore} from "@/providers/store.ts";
import {api} from "@/providers/api.ts";

export const toDoContextActions: ToDoMutations = {
  getList: async () => {
    useStore.getState().dispatch({
      type: AllActionsEnum.TODO_LOADING,
      payload: true,
    });
    const {data, status} = await api.getAll();
    if (status === 200) {
      const payload = {
        list: data,
        error: undefined,
        isLoading: false,
      };
      useStore.getState().dispatch({type: AllActionsEnum.GET_TODO_LIST, payload});
    }
  },
  addItem: async (title: string, description: string) => {
    useStore.getState().dispatch({
      type: AllActionsEnum.TODO_LOADING,
      payload: true,
    });
    const {data, status} = await api.create({title, description});
    if (status === 201) {
      const payload = {
        list: [data],
        error: undefined,
        isLoading: false,
      };
      useStore.getState().dispatch({type: AllActionsEnum.ADD_TODO_ITEM, payload});
    }
  },
  updateItem: async (
    id: number,
    payload: UpdateItemPayload,
  ) => {
    useStore.getState().dispatch({
      type: AllActionsEnum.TODO_ID_LOADING,
      payload: {id},
    });
    const {data, status} = await api.update(id, payload);
    if (status === 200) {
      const payload = {
        list: [data],
        error: undefined,
        isLoading: useStore.getState().toDoList.isLoading,
        isLoadingItem: undefined,
      };
      useStore.getState().dispatch({type: AllActionsEnum.UPDATE_TODO_ITEM, payload});
    }
  },
  deleteItem: async (id: number) => {
    useStore.getState().dispatch({
      type: AllActionsEnum.TODO_LOADING,
      payload: true,
    });
    const {status} = await api._delete(id);
    if (status === 204) {
      useStore.getState().dispatch({type: AllActionsEnum.DELETE_TODO_ITEM, payload: id});
    }
  },
  suggestItem: async(id: number) => {
    useStore.getState().dispatch({
      type: AllActionsEnum.TODO_ID_LOADING,
      payload: {id},
    });
    const {data, status} = await api.generateSuggestion(id);
    if (status === 200) {
      const payload = {
        id,
        suggestion: data,
        isLoadingItem: undefined,
        error: undefined,
      };
      useStore.getState().dispatch({type: AllActionsEnum.SUGGEST_TODO_ITEM, payload});
    }
  },
}

const TODO_ACTIONS = [
  AllActionsEnum.GET_TODO_LIST,
  AllActionsEnum.ADD_TODO_ITEM,
  AllActionsEnum.UPDATE_TODO_ITEM,
];

export const isToDoAction = (action: Actions): action is ToDoActions =>
  (TODO_ACTIONS as string[]).includes(action.type);