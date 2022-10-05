import { Authority } from '@/shared/security/authority';
/* tslint:disable */
// prettier-ignore
const Entities = () => import('@/entities/entities.vue');

const FavoriteAlbum = () => import('@/entities/favorite-album/favorite-album.vue');
const FavoriteAlbumUpdate = () => import('@/entities/favorite-album/favorite-album-update.vue');
const FavoriteAlbumDetails = () => import('@/entities/favorite-album/favorite-album-details.vue');

// jhipster-needle-add-entity-to-router-import - JHipster will import entities to the router here

export default {
  path: '/',
  component: Entities,
  children: [
    {
      path: 'favorite-album',
      name: 'FavoriteAlbum',
      component: FavoriteAlbum,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'favorite-album/new',
      name: 'FavoriteAlbumCreate',
      component: FavoriteAlbumUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'favorite-album/:favoriteAlbumId/edit',
      name: 'FavoriteAlbumEdit',
      component: FavoriteAlbumUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'favorite-album/:favoriteAlbumId/view',
      name: 'FavoriteAlbumView',
      component: FavoriteAlbumDetails,
      meta: { authorities: [Authority.USER] },
    },
    // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
  ],
};
