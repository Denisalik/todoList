import { createRootRoute, Link, Outlet } from "@tanstack/react-router"
    import { TanStackRouterDevtools } from "@tanstack/router-devtools"

    export const Route = createRootRoute({
      component: RootComponent,
    })

    function RootComponent() {
      return (
        <>
          <div className="p-4 flex gap-4 border-b">
            <Link to="/" className="[&.active]:font-bold hover:underline">
              Home
            </Link>
          </div>
          <main className="p-4">
            <Outlet />
          </main>
          <TanStackRouterDevtools />
        </>
      )
    }