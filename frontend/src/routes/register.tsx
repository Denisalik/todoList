import {createFileRoute, Link, useNavigate} from '@tanstack/react-router'
import {useStore} from "@/providers/store.ts";
import {useForm} from "@tanstack/react-form";
import {registerSchema} from "@/providers/zodSchema.ts";
import {authContextActions} from "@/providers/auth.ts";
import {Label} from "@/components/ui/label";
import {Alert, AlertDescription} from "@/components/ui/alert";
import {Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle} from "@/components/ui/card";
import {AlertCircleIcon, LoaderIcon} from "@hugeicons/core-free-icons";
import {HugeiconsIcon} from "@hugeicons/react";
import {Input} from "@/components/ui/input";
import {Button} from "@/components/ui/button";

export const Route = createFileRoute('/register')({
  component: RouteComponent,
})

function RouteComponent() {
  const navigate = useNavigate();
  const {isLoading, error} = useStore(state => state.authentication);
  const form = useForm({
    defaultValues: {
      username: '',
      email: '',
      password: '',
      confirmPassword: '',
    },
    validators: {
      onChange: registerSchema,
    },
    onSubmit: async ({value}) => {
      await authContextActions.register(value.username, value.email, value.password);
      await authContextActions.me();
      await navigate({to: '/'});
    },
  });
  return (
    <Card className="w-full max-w-md mx-auto">
      <CardHeader>
        <CardTitle>Create an account</CardTitle>
        <CardDescription>Enter your details to get started</CardDescription>
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
              <HugeiconsIcon icon={AlertCircleIcon} className="h-4 w-4"/>
              <AlertDescription>
                {error}
              </AlertDescription>
            </Alert>
          )}

          <form.Field
            name="username"
            children={(field) => (
              <div className="space-y-2">
                <Label htmlFor={field.name}>Username</Label>
                <Input
                  id={field.name}
                  name={field.name}
                  value={field.state.value}
                  onBlur={field.handleBlur}
                  onChange={(e) => field.handleChange(e.target.value)}
                />
                {field.state.meta.errors && (
                  <p className="text-sm text-destructive">
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
            name="email"
            children={(field) => (
              <div className="space-y-2">
                <Label htmlFor={field.name}>Email</Label>
                <Input
                  id={field.name}
                  name={field.name}
                  type="email"
                  value={field.state.value}
                  onBlur={field.handleBlur}
                  onChange={(e) => field.handleChange(e.target.value)}
                />
                {field.state.meta.errors && (
                  <p className="text-sm text-destructive">
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
            name="password"
            children={(field) => (
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
                  <p className="text-sm text-destructive">
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
            name="confirmPassword"
            children={(field) => (
              <div className="space-y-2">
                <Label htmlFor={field.name}>Confirm Password</Label>
                <Input
                  id={field.name}
                  name={field.name}
                  type="password"
                  value={field.state.value}
                  onBlur={field.handleBlur}
                  onChange={(e) => field.handleChange(e.target.value)}
                />
                {field.state.meta.errors && (
                  <p className="text-sm text-destructive">
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
            selector={(state) => [state.canSubmit, state.isSubmitting]}
            children={([canSubmit, isSubmitting]) => (
              <Button
                type="submit"
                className="w-full"
                disabled={!canSubmit || isSubmitting || isLoading}
              >
                {(isSubmitting || isLoading) && (
                  <HugeiconsIcon icon={LoaderIcon} className="animate-spin"/>
                )}
                Create Account
              </Button>
            )}
          />
        </form>
      </CardContent>
      <CardFooter className="flex justify-center">
        <p className="text-sm text-muted-foreground">
          Already have an account?{' '}
          <Link to="/login" className="text-primary hover:underline">
            Sign in
          </Link>
        </p>
      </CardFooter>
    </Card>
  );
}
