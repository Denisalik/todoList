import { Logout } from '@hugeicons/core-free-icons';
import { HugeiconsIcon } from '@hugeicons/react';
import { createRootRoute, Link, Outlet, useNavigate } from '@tanstack/react-router';
import { TanStackRouterDevtools } from '@tanstack/router-devtools';
import { Button } from '@/components/ui/button.tsx';
import { Toaster } from '@/components/ui/sonner.tsx';
import { TooltipProvider } from '@/components/ui/tooltip.tsx';
import { authContextActions } from '@/providers/auth.ts';
import { useStore } from '@/providers/store.ts';

export const Route = createRootRoute({
  component: RootComponent,
});

function RootComponent() {
  const { token, user } = useStore((state) => state.authentication);
  const navigate = useNavigate();
  const handleLogout = async () => {
    authContextActions.logout();
    await navigate({ to: '/login' });
  };
  return (
    <>
      <div className="flex border-b">
        <div className="flex flex-10 gap-4 p-4">
          <Link to="/" className="hover:underline [&.active]:font-bold">
            Home
          </Link>
          <Link to="/toDoList" className="hover:underline [&.active]:font-bold">
            ToDoList
          </Link>
          {user?.admin && (
            <Link to="/admin" className="hover:underline [&.active]:font-bold">
              Admin Panel
            </Link>
          )}
        </div>
        {token && (
          <div className="flex flex-2 border-l p-4">
            <Button
              type="button"
              onClick={handleLogout}
              className="w-full bg-gradient-to-r from-emerald-500 to-teal-600 font-medium text-white shadow-emerald-500/25 shadow-md transition-all duration-200 hover:from-emerald-600 hover:to-teal-700 hover:shadow-emerald-500/35 hover:shadow-lg focus-visible:ring-2 focus-visible:ring-emerald-400 focus-visible:ring-offset-2 active:scale-[0.98]"
            >
              <HugeiconsIcon icon={Logout} className="h-4 w-4" />
              Log out
            </Button>
          </div>
        )}
      </div>
      <main className="p-4">
        <TooltipProvider>
          <Outlet />
        </TooltipProvider>
      </main>
      <Toaster />
      <TanStackRouterDevtools />
    </>
  );
}
