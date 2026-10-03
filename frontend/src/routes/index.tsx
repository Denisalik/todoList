import { createFileRoute } from "@tanstack/react-router"

    export const Route = createFileRoute("/")({
      component: HomePage,
    })

    function HomePage() {
      return (
        <div className="space-y-4">
          <h1 className="text-3xl font-bold">Welcome to To Do List</h1>
          <p className="text-muted-foreground">
            React + TypeScript + TanStack + shadcn/ui + Tailwind
          </p>
        </div>
      )
    }