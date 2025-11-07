<template>
    <article class="base-article">
      <div class="base-heading">
        <h1>
          <span style="color:#14C9C9">门票预约</span>
        </h1>
      </div>
      <RouterView />
    </article>
  </template>

  <script setup>
  import { RouterView } from 'vue-router'
  </script>

  <style>
  .base-heading {
    display: flex;
    flex-flow: row nowrap;
    align-items: center;
    justify-content: space-between;
  }
  </style>
  