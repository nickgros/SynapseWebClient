import { defineConfig } from 'vite'
import { nodePolyfills } from 'vite-plugin-node-polyfills'

/**
 * Vite config to generate the ESM & CJS bundles for Synapse React Client.
 */
const config = defineConfig({
  server: { port: 3000 },
  build: {
    lib: {
      entry: 'js/swc-modules.ts',
      fileName: 'swc-modules',
      formats: ['es'],
    },
    outDir: './src/main/webapp/generated',
    emptyOutDir: false,
    commonjsOptions: {
      transformMixedEsModules: true,
    },
    rollupOptions: {
      onwarn(warning, warn) {
        // Suppress "Module level directives cause errors when bundled" warnings
        if (warning.code === 'MODULE_LEVEL_DIRECTIVE') {
          return
        }
        warn(warning)
      },
    },
  },
  resolve: {
    dedupe: ['react', 'react-dom'],
  },
  plugins: [nodePolyfills()],
  define: {
    __TEST__: JSON.stringify(false),
    __DEV__: JSON.stringify(false),
  },
})

export default config
