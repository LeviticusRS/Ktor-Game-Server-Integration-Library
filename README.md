# EventSender

`RSPS Panel API Bridge` is a Kotlin library for sending events to a server using HTTP POST requests. It is designed to be used in a Java game server environment but can be adapted for other use cases as well.

## Table of Contents

- [Features](#features)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)

## Features

- Asynchronously send events to a server using HTTP POST requests.
- Queue events for later processing.
- Automatically retry sending events on failure.
- Supports concurrent event processing.
- Efficiently uses coroutine-based asynchronous programming.

## Getting Started

### Prerequisites

To use EventSender, you need:

- Kotlin or Java environment.
- A server URL where events will be sent.
- An API key for authentication.

### Installation

Add `RSPS Panel API Bridge` to your project by including the project as a submodule or create a jar file, and include it in your project.
