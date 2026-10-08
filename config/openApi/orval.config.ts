// @ts-ignore
import { defineConfig } from 'orval';
// https://orval.dev/docs/guides/custom-axios/
export default defineConfig({
    client: {
        input: '/app/openapi.json',
        output: {
            target: '/app/frontend/src/generated/openapi.ts',
            schemas: '/app/frontend/src/generated/model',
            client: 'axios',
        }
    },
});