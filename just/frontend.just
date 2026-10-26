[working-directory: 'frontend']
frontend-install:
    pnpm install

[working-directory: 'frontend']
frontend-start:
    pnpm dev

[working-directory: 'frontend']
frontend-lint:
    pnpm lint

[working-directory: 'frontend']
frontend-list-dependencies:
    pnpm list --depth 0

[working-directory: 'frontend']
frontend-list-scripts:
    pnpm run

[working-directory: 'frontend']
frontend-list-manifest:
    pnpm pkg get