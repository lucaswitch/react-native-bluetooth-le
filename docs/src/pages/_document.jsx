import Document, { Html, Head, Main, NextScript } from "next/document";

export default class MyDocument extends Document{
  render () {
    return (
      <Html>
        <Head>
          <link rel="preconnect" href="https://fonts.googleapis.com" />
          <link rel="preconnect" href="https://fonts.gstatic.com" />
          <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;700;900&display=swap" rel="stylesheet" />
          <link href="https://cdn.jsdelivr.net/npm/daisyui@2.13.6/dist/full.css" rel="stylesheet" type="text/css" />
          <link rel="stylesheet" href="https://unpkg.com/dracula-prism/dist/css/dracula-prism.css"
          ></link>
          <link rel="shortcut icon" href="https://flowbite.com/docs/images/logo.svg" />
        </Head>
        <body>
        <Main />
        <NextScript />
        </body>
      </Html>
    )
  }
}