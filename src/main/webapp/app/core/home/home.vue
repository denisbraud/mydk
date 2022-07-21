<template>
  <div class="home">
    <div v-if="authenticated">
      <form role="search" novalidate v-on:submit.prevent="search()">
        <div class="row no-gutters">
          <div class="col-lg-4 col-xl-4 pr-1 pb-1">
            <input type="text" v-model="artistName" class="form-control" placeholder="Artiste" />
          </div>
          <div class="col-lg-3 col-xl-4 pr-1 pb-1"><input type="text" v-model="albumName" class="form-control" placeholder="Album" /></div>
          <div class="col-lg-1 col-xl-1 pr-1 pb-1">
            <div class="form-group form-check pt-1">
              <input type="checkbox" class="form-check-input" id="mainAlbumOnly" v-model="mainAlbumOnly" />
              <label class="form-check-label" for="mainAlbumOnly" title="Albums principaux uniquement">Principaux</label>
            </div>
          </div>
          <div class="col-lg-4 col-xl-3 pr-1 pb-1 text-right">
            <font-awesome-icon icon="sync" :spin="isLoading" v-if="isLoading"></font-awesome-icon>
            <button class="btn btn-primary" type="submit" :disabled="isLoading">
              <font-awesome-icon icon="search"></font-awesome-icon> Rechercher
            </button>
            <button class="btn btn-primary" v-on:click="loadFavorite" :disabled="isLoading">
              <font-awesome-icon icon="compact-disc"></font-awesome-icon> My Discotek
            </button>
          </div>
        </div>
      </form>
      <div class="row mt-3" v-if="albums">
        <div class="col-md-6 col-lg-3 col-xl-2 mb-1" v-for="album in albums" :key="album.spotifyId">
          <a :href="'https://open.spotify.com/album/' + album.spotifyId" target="_blank" class="imghover">
            <img :src="album.imageUrl" alt="x" class="img-fluid border" />
            <span class="text-dark">
              {{ album.artistName }} · {{ album.name }}<br />
              <small>
                {{ album.releaseDate }} · {{ album.totalTracks }} titres<br />
                {{ album.type }} {{ album.nameSuffix }} </small
              ><br /><br />
              <router-link
                v-if="album.favoriteAlbumId"
                :to="{ name: 'FavoriteAlbumEdit', params: { favoriteAlbumId: album.favoriteAlbumId } }"
                custom
                v-slot="{ navigate }"
              >
                <button @click="navigate" class="btn btn-outline-primary btn-sm edit" :disabled="isLoading">
                  <font-awesome-icon icon="compact-disc"></font-awesome-icon> Modifier / Supprimer
                </button>
              </router-link>
              <router-link
                v-else
                :to="{ name: 'FavoriteAlbumCreate', params: { login: username, album: album } }"
                custom
                v-slot="{ navigate }"
              >
                <button
                  @click="navigate"
                  class="btn btn-outline-primary btn-sm jh-create-entity create-favorite-album"
                  :disabled="isLoading"
                >
                  <font-awesome-icon icon="compact-disc"></font-awesome-icon> Ajouter
                </button>
              </router-link>
            </span> </a
          ><br />
          <small>
            {{ album.releaseDate }}
            <span v-if="album.favoriteAlbumId" class="text-primary"
              ><font-awesome-icon icon="compact-disc"></font-awesome-icon> {{ album.comment }}</span
            >
          </small>
        </div>
      </div>
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
        <router-link class="alert-link" to="/register" v-text="$t('global.messages.info.register.link')"
          >Register a new account</router-link
        >
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./home.component.ts"></script>
