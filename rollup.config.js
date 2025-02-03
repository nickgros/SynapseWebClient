/* eslint-disable no-undef */
import nodeResolve from '@rollup/plugin-node-resolve'
import typescript from '@rollup/plugin-typescript'
import terser from '@rollup/plugin-terser'
import css from 'rollup-plugin-import-css'
import json from '@rollup/plugin-json'

export default input => ({
  input,
  output: {
    dir: './src/main/webapp/generated/swc-modules.js',
    sourcemap: false,
    format: 'es',
  },
  jsx: 'react-jsx',
  plugins: [
    nodeResolve(),
    typescript(),
    json(),
    css(),
    terser({
      output: {
        comments: false,
      },
    }),
  ],
})
