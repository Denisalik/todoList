import {toDoContextActions} from "@/providers/toDoList.ts";
import {useForm} from "@tanstack/react-form";
import {createToDoItemSchema} from "@/providers/zodSchema.ts";
import { Input } from "@/components/ui/input.tsx";
import {Button} from "@/components/ui/button.tsx";
import {HugeiconsIcon} from "@hugeicons/react";
import {Add01Icon} from "@hugeicons/core-free-icons";
import type * as React from "react";


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
      className="flex gap-2 flex-col sm:flex-row sm:items-start"
    >
      <form.Field
        name="title"
        children={(field) => (
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
              <p className="text-sm text-destructive text-ellipsis">
                {field.state.meta.errors
                  .map(err => err?.message)
                  .filter(e => e !== undefined)
                  .join(', ')}
              </p>
            )}
          </div>
        )}
      />

      <form.Field
        name="description"
        children={(field) => (
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
              <p className="text-sm text-destructive text-ellipsis">
                {field.state.meta.errors
                  .map(err => err?.message)
                  .filter(e => e !== undefined)
                  .join(', ')}
              </p>
            )}
          </div>
        )}
      />
      <form.Subscribe
        selector={(s) => [s.canSubmit, s.isSubmitting]}
        children={([canSubmit, isSubmitting]) => (
          <Button className="flex-1" type="submit" disabled={!canSubmit || isSubmitting}>
            <HugeiconsIcon icon={Add01Icon} size={18} />
            Add
          </Button>
        )}
      />
    </form>
  );
}
