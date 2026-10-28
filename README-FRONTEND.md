# Frontend To Do List
## Getting Started
To run this application:

```bash
#from /
just frontend-install
just frontend-start
#or from /frontend
pnpm install
pnpm dev
```
# Building For Production
To build this application for production:

```bash
pnpm build
```
## Linting & Formatting
This project uses [Biome](https://biomejs.dev/) for linting and formatting.
The following scripts are available:

```bash
pnpm lint
#or if need fix
pnpm fix
```

## Routing

This project uses [TanStack Router](https://tanstack.com/router) with file-based routing.
Routes are managed as files in `src/routes`.
