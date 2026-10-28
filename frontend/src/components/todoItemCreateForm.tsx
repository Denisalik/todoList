import { Add01Icon } from '@hugeicons/core-free-icons';
import { HugeiconsIcon } from '@hugeicons/react';
import { useForm } from '@tanstack/react-form';
import type * as React from 'react';
import { Button } from '@/components/ui/button.tsx';
import { Input } from '@/components/ui/input.tsx';
import { toDoContextActions } from '@/providers/toDoList.ts';
import { createToDoItemSchema } from '@/providers/zodSchema.ts';

export const TodoItemCreateForm: React.FC = () => {
  const form = useForm({
    defaultValues: { title: '', description: '' },
    validators: { onChange: createToDoItemSchema },
    onSubmit: async ({ value, formApi }) => {
      await toDoContextActions.addItem(value.title.trim(), value.description.trim());
      formApi.reset();
    },
  });

  return (
    <form
      onSubmit={async (e) => {
        e.preventDefault();
        e.stopPropagation();
        await form.handleSubmit();
      }}
      className="flex flex-col gap-2 sm:flex-row sm:items-start"
    >
      <form.Field name="title">
        {(field) => (
          <div className="flex-4">
            <Input
              id={field.name}
              name={field.name}
              placeholder="What needs to be done?"
              value={field.state.value}
              onBlur={field.handleBlur}
              onChange={(e) => field.handleChange(e.target.value)}
              aria-invalid={!!field.state.meta.errors.length}
            />
            {field.state.meta.errors && (
              <p className="text-ellipsis text-destructive text-sm">
                {field.state.meta.errors
                  .map((err) => err?.message)
                  .filter((e) => e !== undefined)
                  .join(', ')}
              </p>
            )}
          </div>
        )}
      </form.Field>

      <form.Field name="description">
        {(field) => (
          <div className="flex-7">
            <Input
              id={field.name}
              name={field.name}
              placeholder="Description"
              value={field.state.value}
              onBlur={field.handleBlur}
              onChange={(e) => field.handleChange(e.target.value)}
            />
            {field.state.meta.errors && (
              <p className="text-ellipsis text-destructive text-sm">
                {field.state.meta.errors
                  .map((err) => err?.message)
                  .filter((e) => e !== undefined)
                  .join(', ')}
              </p>
            )}
          </div>
        )}
      </form.Field>
      <form.Subscribe selector={(s) => [s.canSubmit, s.isSubmitting]}>
        {([canSubmit, isSubmitting]) => (
          <Button className="flex-1" type="submit" disabled={!canSubmit || isSubmitting}>
            <HugeiconsIcon icon={Add01Icon} size={18} />
            Add
          </Button>
        )}
      </form.Subscribe>
    </form>
  );
};
