import {
  Cancel01Icon,
  CheckmarkCircle02Icon,
  CircleIcon,
  Delete02Icon,
  Loading03Icon,
  PencilEdit01Icon,
  SparklesIcon,
  Tick02Icon,
} from '@hugeicons/core-free-icons';
import { HugeiconsIcon } from '@hugeicons/react';
import type * as React from 'react';
import { useEffect, useState } from 'react';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Tooltip, TooltipContent, TooltipTrigger } from '@/components/ui/tooltip.tsx';
import type { ToDoItem as ToDoItemType } from '@/generated/model';
import { showMessage } from '@/providers/message.ts';
import { toDoContextActions } from '@/providers/toDoList.ts';

interface ToDoItemProps {
  todo: ToDoItemType;
  loading: boolean;
}

export const ToDoItem: React.FC<ToDoItemProps> = ({ todo, loading }) => {
  const [editing, setEditing] = useState(false);
  const [updatedTitle, setUpdatedTitle] = useState(todo.title);
  const [updatedDescription, setUpdatedDescription] = useState<string>(todo.description ?? '');
  const [updatedSelected, setUpdatedSelected] = useState<boolean>(todo.selected ?? false);

  const save = async () => {
    setEditing(false);
    const payload = {
      title: updatedTitle !== todo.title ? updatedTitle : undefined,
      description: updatedDescription !== todo.description ? updatedDescription : undefined,
      selected: updatedSelected !== todo.selected ? updatedSelected : undefined,
    };
    await toDoContextActions.updateItem(todo.id!, payload);
    showMessage.success('Updated ToDo Item');
  };
  const deleteItem = async () => {
    setEditing(false);
    await toDoContextActions.deleteItem(todo.id!);
    showMessage.success('Deleted ToDo Item');
  };
  const generateAnswer = async () => {
    setEditing(false);
    await toDoContextActions.suggestItem(todo.id!);
    showMessage.success('Generate todo Answer');
  };
  // biome-ignore lint/correctness/useExhaustiveDependencies(save): save is recreated every render, adding it causes infinite loop
  useEffect(() => {
    if (updatedSelected === todo.selected) return;
    save();
  }, [updatedSelected, todo.selected]);

  const liClassName =
    'group flex items-center gap-3 rounded-lg border bg-card px-3 py-2 transition hover:shadow-sm';

  return loading ? (
    <li className={liClassName}>
      <div className="flex w-full items-center justify-center py-1">
        <HugeiconsIcon
          icon={Loading03Icon}
          size={22}
          className="shrink-0 animate-spin text-primary"
        />
      </div>
    </li>
  ) : (
    <li className={liClassName}>
      <button
        type="button"
        onClick={() => setUpdatedSelected(!updatedSelected)}
        className="shrink-0 text-primary transition-colors hover:text-primary"
        aria-label={updatedSelected ? 'Mark as not done' : 'Mark as done'}
      >
        <HugeiconsIcon icon={updatedSelected ? CheckmarkCircle02Icon : CircleIcon} size={22} />
      </button>

      <div className="min-w-0 flex-1">
        {editing ? (
          <div className="flex items-center gap-2">
            <Input
              autoFocus
              value={updatedTitle}
              onChange={(e) => setUpdatedTitle(e.target.value)}
              onKeyDown={(e) => {
                if (e.key === 'Enter') save();
                if (e.key === 'Escape') {
                  setUpdatedTitle(todo.title ?? '');
                  setUpdatedDescription(todo.description ?? '');
                  setEditing(false);
                }
              }}
            />
            <Input
              value={updatedDescription}
              onChange={(e) => setUpdatedDescription(e.target.value)}
              onKeyDown={(e) => {
                if (e.key === 'Enter') save();
                if (e.key === 'Escape') {
                  setUpdatedTitle(todo.title ?? '');
                  setUpdatedDescription(todo.description ?? '');
                  setEditing(false);
                }
              }}
            />
            <Button size="icon" variant="ghost" onClick={save}>
              <HugeiconsIcon icon={Tick02Icon} size={18} />
            </Button>
            <Button
              size="icon"
              variant="ghost"
              onClick={() => {
                setUpdatedTitle(todo.title ?? '');
                setUpdatedDescription(todo.description ?? '');
                setEditing(false);
              }}
            >
              <HugeiconsIcon icon={Cancel01Icon} size={18} />
            </Button>
          </div>
        ) : (
          <>
            <p className="whitespace-pre-wrap break-words font-semibold text-black text-sm">
              {updatedTitle}
            </p>
            <p className="mt-1 whitespace-pre-wrap break-words text-foreground text-xs">
              {updatedDescription}
            </p>
            {todo.answer && (
              <div className="mt-2 rounded-md border-muted border-l-2 bg-muted/40 px-3 py-2">
                <p className="whitespace-pre-wrap break-words text-foreground text-xs leading-relaxed">
                  {todo.answer.content}
                </p>
              </div>
            )}
          </>
        )}
      </div>

      <div className="flex shrink-0 items-center gap-1 opacity-0 transition-opacity group-hover:opacity-100">
        <Tooltip>
          <TooltipTrigger
            render={
              <Button
                size="icon"
                variant="ghost"
                onClick={generateAnswer}
                aria-label="Generate with AI"
              >
                <HugeiconsIcon icon={SparklesIcon} size={16} />
              </Button>
            }
          />
          <TooltipContent>
            <p>Generate what to do</p>
          </TooltipContent>
        </Tooltip>
        <Tooltip>
          <TooltipTrigger
            render={
              <Button
                size="icon"
                variant="ghost"
                onClick={() => setEditing(true)}
                aria-label="Edit"
              >
                <HugeiconsIcon icon={PencilEdit01Icon} size={16} />
              </Button>
            }
          />
          <TooltipContent>
            <p>Edit to do</p>
          </TooltipContent>
        </Tooltip>
        <Tooltip>
          <TooltipTrigger
            render={
              <Button size="icon" variant="ghost" onClick={deleteItem} aria-label="Delete">
                <HugeiconsIcon icon={Delete02Icon} size={16} className="text-destructive" />
              </Button>
            }
          />
          <TooltipContent>
            <p>Remove to do</p>
          </TooltipContent>
        </Tooltip>
      </div>
    </li>
  );
};
