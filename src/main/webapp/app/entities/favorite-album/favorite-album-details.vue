<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="favoriteAlbum">
        <h2 class="jh-entity-heading" data-cy="favoriteAlbumDetailsHeading">
          <span v-text="$t('mydkApp.favoriteAlbum.detail.title')">FavoriteAlbum</span> {{ favoriteAlbum.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('mydkApp.favoriteAlbum.login')">Login</span>
          </dt>
          <dd>
            <span>{{ favoriteAlbum.login }}</span>
          </dd>
          <dt>
            <span v-text="$t('mydkApp.favoriteAlbum.albumSpotifyId')">Album Spotify Id</span>
          </dt>
          <dd>
            <span>{{ favoriteAlbum.albumSpotifyId }}</span>
          </dd>
          <dt>
            <span v-text="$t('mydkApp.favoriteAlbum.rank')">Rank</span>
          </dt>
          <dd>
            <span>{{ favoriteAlbum.rank }}</span>
          </dd>
          <dt>
            <span v-text="$t('mydkApp.favoriteAlbum.comment')">Comment</span>
          </dt>
          <dd>
            <span>{{ favoriteAlbum.comment }}</span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="favoriteAlbum.id"
          :to="{ name: 'FavoriteAlbumEdit', params: { favoriteAlbumId: favoriteAlbum.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./favorite-album-details.component.ts"></script>
