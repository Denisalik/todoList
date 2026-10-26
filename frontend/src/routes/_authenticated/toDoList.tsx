import {createFileRoute} from '@tanstack/react-router'
import {useStore} from "@/providers/store.ts";
import {toDoContextActions} from "@/providers/toDoList.ts";
import {ToDoItem} from "@/components/toDoItem.tsx";
import {TodoItemCreateForm} from "@/components/todoItemCreateForm.tsx";

export const Route = createFileRoute('/_authenticated/toDoList')({
    loader: async () => {
        await toDoContextActions.getList();
    },
    component: RouteComponent,
})

function RouteComponent() {
    const {list, isLoadingItem} = useStore(state => state.toDoList);
    return (<>
        <TodoItemCreateForm/>
        <br/>
        {list.map(todo => <ToDoItem key={todo.id} todo={todo} loading={isLoadingItem?.id===todo.id}/>)}
    </>);
}
