import { Component, Vue, Inject } from 'vue-property-decorator';

import { IFavoriteAlbum } from '@/shared/model/favorite-album.model';
import FavoriteAlbumService from './favorite-album.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class FavoriteAlbumDetails extends Vue {
  @Inject('favoriteAlbumService') private favoriteAlbumService: () => FavoriteAlbumService;
  @Inject('alertService') private alertService: () => AlertService;

  public favoriteAlbum: IFavoriteAlbum = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.favoriteAlbumId) {
        vm.retrieveFavoriteAlbum(to.params.favoriteAlbumId);
      }
    });
  }

  public retrieveFavoriteAlbum(favoriteAlbumId) {
    this.favoriteAlbumService()
      .find(favoriteAlbumId)
      .then(res => {
        this.favoriteAlbum = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
