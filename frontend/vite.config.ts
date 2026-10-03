import path from "path";
import { defineConfig } from "vite";
import react, { reactCompilerPreset } from "@vitejs/plugin-react";
import { tanstackRouter } from "@tanstack/router-plugin/vite";
import tailwindcss from "@tailwindcss/vite";
import babel from "@rolldown/plugin-babel";
// import viteCompression from 'vite-plugin-compression';

export default defineConfig({
    plugins: [
        tanstackRouter({ target: "react", autoCodeSplitting: true }),
        tailwindcss(),
        react(),
        babel({
            presets: [reactCompilerPreset()],
        })
    ],
    resolve: {
        alias: {
            "@": path.resolve(import.meta.dirname, "./src"),
        },
    },
});