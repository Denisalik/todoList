import { defineConfig } from 'orval';
// todo
// https://orval.dev/docs/guides/basics
// https://orval.dev/docs/guides/react-query/
// https://orval.dev/docs/guides/custom-axios/
// https://orval.dev/docs/guides/client-with-zod/
export default defineConfig({
    client: {
        input: './openapi.json',
        output: {
            target: '/app/frontend/src/generated/openapi.ts',
            schemas: '/app/frontend/src/generated/model',
            client: 'react-query',
        }
    },
});