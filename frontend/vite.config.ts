import path from "path";
import { defineConfig } from "vite";
import react, { reactCompilerPreset } from "@vitejs/plugin-react";
import { tanstackRouter } from "@tanstack/router-plugin/vite";
import tailwindcss from "@tailwindcss/vite";
import babel from "@rolldown/plugin-babel";

export default defineConfig({
    server: {
        port: Number(process.env.VITE_PORT),
        strictPort: true,
    },
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