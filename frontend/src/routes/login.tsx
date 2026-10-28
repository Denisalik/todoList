import { AlertCircleIcon, LoaderIcon } from '@hugeicons/core-free-icons';
import { HugeiconsIcon } from '@hugeicons/react';
import { useForm } from '@tanstack/react-form';
import { createFileRoute, Link, type SearchSchemaInput, useNavigate } from '@tanstack/react-router';
import { Alert, AlertDescription } from '@/components/ui/alert';
import { Button } from '@/components/ui/button.tsx';
import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from '@/components/ui/card';
import { Input } from '@/components/ui/input.tsx';
import { Label } from '@/components/ui/label';
import { authContextActions } from '@/providers/auth.ts';
import { useStore } from '@/providers/store.ts';
import { loginSchema } from '@/providers/zodSchema.ts';

interface LoginSearch {
  redirect?: string;
}

export const Route = createFileRoute('/login')({
  component: RouteComponent,
  validateSearch: (search: LoginSearch & SearchSchemaInput) => ({
    redirect:
      typeof search.redirect === 'string' && search.redirect.startsWith('/')
        ? search.redirect
        : '/',
  }),
});

function RouteComponent() {
  const { redirect } = Route.useSearch();
  const navigate = useNavigate();
  const { isLoading, error } = useStore((state) => state.authentication);
  const form = useForm({
    defaultValues: {
      username: '',
      password: '',
    },
    validators: {
      onChange: loginSchema,
    },
    onSubmit: async ({ value }) => {
      await authContextActions.login(value.username, value.password);
      await authContextActions.me();
      await navigate({ to: redirect });
    },
  });
  return (
    <Card className="mx-auto w-full max-w-md">
      <CardHeader>
        <CardTitle>Login</CardTitle>
        <CardDescription>Enter your credentials to access your account</CardDescription>
      </CardHeader>
      <CardContent>
        <form
          onSubmit={async (e) => {
            e.preventDefault();
            e.stopPropagation();
            await form.handleSubmit();
          }}
          className="space-y-4"
        >
          {error !== undefined && (
            <Alert variant="destructive">
              <HugeiconsIcon icon={AlertCircleIcon} className="h-4 w-4" />
              <AlertDescription>{error}</AlertDescription>
            </Alert>
          )}

          <form.Field name="username">
            {(field) => (
              <div className="space-y-2">
                <Label htmlFor={field.name}>Username</Label>
                <Input
                  id={field.name}
                  name={field.name}
                  type="text"
                  placeholder="username"
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

          <form.Field name="password">
            {(field) => (
              <div className="space-y-2">
                <Label htmlFor={field.name}>Password</Label>
                <Input
                  id={field.name}
                  name={field.name}
                  type="password"
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

          <form.Subscribe selector={(state) => [state.canSubmit, state.isSubmitting]}>
            {([canSubmit, isSubmitting]) => (
              <Button
                type="submit"
                className="w-full"
                disabled={!canSubmit || isSubmitting || isLoading}
              >
                {(isSubmitting || isLoading) && (
                  <HugeiconsIcon icon={LoaderIcon} className="animate-spin" />
                )}
                Sign In
              </Button>
            )}
          </form.Subscribe>
        </form>
      </CardContent>
      <CardFooter className="flex justify-center">
        <p className="text-muted-foreground text-sm">
          Don't have an account?{' '}
          <Link to="/register" className="text-primary hover:underline">
            Sign up
          </Link>
        </p>
      </CardFooter>
    </Card>
  );
}
