import {createRootRoute, Link, Outlet, useNavigate} from "@tanstack/react-router";
import { TanStackRouterDevtools } from "@tanstack/router-devtools";
import {Button} from "@/components/ui/button.tsx";
import {Logout} from "@hugeicons/core-free-icons";
import {HugeiconsIcon} from "@hugeicons/react";
import {authContextActions} from "@/providers/auth.ts";
import {useStore} from "@/providers/store.ts";
import {Toaster} from "@/components/ui/sonner.tsx";
import {TooltipProvider} from "@/components/ui/tooltip.tsx";

export const Route = createRootRoute({
  component: RootComponent,
});

function RootComponent() {
  const {token, user} = useStore(state => state.authentication);
  const navigate = useNavigate();
  const handleLogout = async () => {
    authContextActions.logout();
    await navigate({to: '/login'})
  }
  return (
    <>
      <div className="flex border-b">
        <div className="flex-10 p-4 flex gap-4">
          <Link to="/" className="[&.active]:font-bold hover:underline">
            Home
          </Link>
          <Link to="/toDoList" className="[&.active]:font-bold hover:underline">
            ToDoList
          </Link>
          {user?.admin && (
            <Link to="/admin" className="[&.active]:font-bold hover:underline">
              Admin Panel
            </Link>
          )}
        </div>
        {token && <div className="flex-2 p-4 flex border-l">
            <Button
                type="button"
                onClick={handleLogout}
                className="w-full bg-gradient-to-r from-emerald-500 to-teal-600 text-white font-medium shadow-md
                shadow-emerald-500/25 hover:from-emerald-600 hover:to-teal-700 hover:shadow-lg hover:shadow-emerald-500/35
                active:scale-[0.98] transition-all duration-200 focus-visible:ring-2 focus-visible:ring-emerald-400 focus-visible:ring-offset-2"
            >
                <HugeiconsIcon icon={Logout} className="h-4 w-4"/>
                Log out
            </Button>
        </div>
        }
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
