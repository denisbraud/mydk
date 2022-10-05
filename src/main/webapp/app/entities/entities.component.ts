import { Component, Provide, Vue } from 'vue-property-decorator';

import UserService from '@/entities/user/user.service';
import FavoriteAlbumService from './favorite-album/favorite-album.service';
// jhipster-needle-add-entity-service-to-entities-component-import - JHipster will import entities services here

@Component
export default class Entities extends Vue {
  @Provide('userService') private userService = () => new UserService();
  @Provide('favoriteAlbumService') private favoriteAlbumService = () => new FavoriteAlbumService();
  // jhipster-needle-add-entity-service-to-entities-component - JHipster will import entities services here
}
