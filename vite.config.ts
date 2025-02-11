import { defineConfig } from 'vite'
import { nodePolyfills } from 'vite-plugin-node-polyfills'

/**
 * Vite config to generate the ESM & CJS bundles for Synapse React Client.
 */
const config = defineConfig({
  server: {
    // port: 5173,
    cors: {
      // the origin you will be accessing via browser
      origin: ['http://localhost:8888', 'http://127.0.0.1:8888'],
    },
  },
  // optimizeDeps: {
  //   exclude: ['react', 'react-dom'],
  // },
  build: {
    manifest: true,
    outDir: './src/main/webapp/generated/vite',
    // lib: {
    //   entry: 'js/swc-modules.ts',
    //   fileName: 'swc-modules',
    //   formats: ['es'],
    // },
    // outDir: './src/main/webapp/generated',
    // emptyOutDir: false,
    // commonjsOptions: {
    //   transformMixedEsModules: true,
    // },
    rollupOptions: {
      input: 'js/main.ts',
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
