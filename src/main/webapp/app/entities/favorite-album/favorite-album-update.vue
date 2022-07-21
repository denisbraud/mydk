<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="mydkApp.favoriteAlbum.home.createOrEditLabel"
          data-cy="FavoriteAlbumCreateUpdateHeading"
          v-text="$t('mydkApp.favoriteAlbum.home.createOrEditLabel')"
        >
          Create or edit a FavoriteAlbum
        </h2>
        <div>
          <div class="form-group" v-if="favoriteAlbum.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="favoriteAlbum.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('mydkApp.favoriteAlbum.login')" for="favorite-album-login">Login</label>
            <input
              type="text"
              class="form-control"
              name="login"
              id="favorite-album-login"
              data-cy="login"
              :class="{ valid: !$v.favoriteAlbum.login.$invalid, invalid: $v.favoriteAlbum.login.$invalid }"
              v-model="$v.favoriteAlbum.login.$model"
              required
            />
            <div v-if="$v.favoriteAlbum.login.$anyDirty && $v.favoriteAlbum.login.$invalid">
              <small class="form-text text-danger" v-if="!$v.favoriteAlbum.login.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.favoriteAlbum.login.maxLength"
                v-text="$t('entity.validation.maxlength', { max: 50 })"
              >
                This field cannot be longer than 50 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('mydkApp.favoriteAlbum.albumSpotifyId')" for="favorite-album-albumSpotifyId"
              >Album Spotify Id</label
            >
            <input
              type="text"
              class="form-control"
              name="albumSpotifyId"
              id="favorite-album-albumSpotifyId"
              data-cy="albumSpotifyId"
              :class="{ valid: !$v.favoriteAlbum.albumSpotifyId.$invalid, invalid: $v.favoriteAlbum.albumSpotifyId.$invalid }"
              v-model="$v.favoriteAlbum.albumSpotifyId.$model"
              required
            />
            <div v-if="$v.favoriteAlbum.albumSpotifyId.$anyDirty && $v.favoriteAlbum.albumSpotifyId.$invalid">
              <small
                class="form-text text-danger"
                v-if="!$v.favoriteAlbum.albumSpotifyId.required"
                v-text="$t('entity.validation.required')"
              >
                This field is required.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.favoriteAlbum.albumSpotifyId.maxLength"
                v-text="$t('entity.validation.maxlength', { max: 255 })"
              >
                This field cannot be longer than 255 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('mydkApp.favoriteAlbum.rank')" for="favorite-album-rank">Rank</label>
            <input
              type="number"
              class="form-control"
              name="rank"
              id="favorite-album-rank"
              data-cy="rank"
              :class="{ valid: !$v.favoriteAlbum.rank.$invalid, invalid: $v.favoriteAlbum.rank.$invalid }"
              v-model.number="$v.favoriteAlbum.rank.$model"
              required
            />
            <div v-if="$v.favoriteAlbum.rank.$anyDirty && $v.favoriteAlbum.rank.$invalid">
              <small class="form-text text-danger" v-if="!$v.favoriteAlbum.rank.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
              <small class="form-text text-danger" v-if="!$v.favoriteAlbum.rank.numeric" v-text="$t('entity.validation.number')">
                This field should be a number.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('mydkApp.favoriteAlbum.comment')" for="favorite-album-comment">Comment</label>
            <input
              type="text"
              class="form-control"
              name="comment"
              id="favorite-album-comment"
              data-cy="comment"
              :class="{ valid: !$v.favoriteAlbum.comment.$invalid, invalid: $v.favoriteAlbum.comment.$invalid }"
              v-model="$v.favoriteAlbum.comment.$model"
            />
            <div v-if="$v.favoriteAlbum.comment.$anyDirty && $v.favoriteAlbum.comment.$invalid">
              <small
                class="form-text text-danger"
                v-if="!$v.favoriteAlbum.comment.maxLength"
                v-text="$t('entity.validation.maxlength', { max: 255 })"
              >
                This field cannot be longer than 255 characters.
              </small>
            </div>
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" v-on:click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.cancel')">Cancel</span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="$v.favoriteAlbum.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./favorite-album-update.component.ts"></script>
