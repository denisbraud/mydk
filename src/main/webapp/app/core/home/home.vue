<template>
  <div class="home">
    <div v-if="authenticated">
      <span v-if="username" v-text="$t('home.logged.message', { username: username })">Vous êtes connecté en tant que "{{ username }}"</span>
    </div>

    <div class="card jh-card" v-if="!authenticated">
      <h1 class="display-4">My Discotek</h1>
      <p class="lead mb-5">Retrouvez facilement vos albums préférés en les ajoutant à votre Discotek.</p>
      <div class="alert alert-info">
        <span v-text="$t('global.messages.info.authenticated.prefix')">Vous pouvez vous</span>
        <a class="alert-link" v-on:click="openLogin()" v-text="$t('global.messages.info.authenticated.link')">connecter</a>...
      </div>
      <div class="alert alert-warning">
        <span v-text="$t('global.messages.info.register.noaccount')">You don't have an account yet?</span>&nbsp;
        <router-link class="alert-link" to="/register" v-text="$t('global.messages.info.register.link')">Register a new account</router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./home.component.ts"></script>
