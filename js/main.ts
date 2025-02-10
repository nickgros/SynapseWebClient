/**
 * Synapse Web Client JavaScript modules
 *
 * JavaScript dependencies that should be loaded to the global scope in SWC can be added here.
 *
 * This file will be bundled and loaded in the SWC application. Excess imports should be avoided to keep this
 * bundle as small as possible.
 */
// https://vite.dev/guide/backend-integration
import 'vite/modulepreload-polyfill'
import React from 'react'
import ReactDOM from 'react-dom'
import ReactDOMClient from 'react-dom/client'
// import * as SRC from 'synapse-react-client'
import * as ReactQuery from '@tanstack/react-query'
// import Grid2 from '@mui/material/Grid2'
// import * as Moment from 'moment'

self.React = React
self.ReactDOM = ReactDOM
// @ts-expect-error
self.ReactDOMClient = ReactDOMClient
// @ts-expect-error
self.ReactQuery = ReactQuery

// @ts-expect-error
// self.Moment = Moment

// const MaterialUI = {
//   Grid2,
// }

// @ts-expect-error
// self.MaterialUI = MaterialUI
// @ts-expect-error
// self.SRC = SRC
