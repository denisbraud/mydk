import { Component, Vue, Inject } from 'vue-property-decorator';

import { required, maxLength, numeric } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import { IFavoriteAlbum, FavoriteAlbum } from '@/shared/model/favorite-album.model';
import FavoriteAlbumService from './favorite-album.service';

const validations: any = {
  favoriteAlbum: {
    login: {
      required,
      maxLength: maxLength(50),
    },
    albumSpotifyId: {
      required,
      maxLength: maxLength(255),
    },
    rank: {
      required,
      numeric,
    },
    comment: {
      maxLength: maxLength(255),
    },
  },
};

@Component({
  validations,
})
export default class FavoriteAlbumUpdate extends Vue {
  @Inject('favoriteAlbumService') private favoriteAlbumService: () => FavoriteAlbumService;
  @Inject('alertService') private alertService: () => AlertService;

  public favoriteAlbum: IFavoriteAlbum = new FavoriteAlbum();
  public isSaving = false;
  public currentLanguage = '';
  private removeId: number = null;

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.favoriteAlbumId) {
        vm.retrieveFavoriteAlbum(to.params.favoriteAlbumId);
      } else {
        vm.favoriteAlbum.login = to.params.login;
        if (to.params.album) {
          vm.favoriteAlbum.albumSpotifyId = to.params.album.spotifyId;
          vm.favoriteAlbum.album = to.params.album;
          vm.favoriteAlbum.rank = 50;
        }
      }
    });
  }
  created(): void {
    this.currentLanguage = this.$store.getters.currentLanguage;
    this.$store.watch(
      () => this.$store.getters.currentLanguage,
      () => {
        this.currentLanguage = this.$store.getters.currentLanguage;
      }
    );
  }
  public save(): void {
    this.isSaving = true;
    if (this.favoriteAlbum.id) {
      this.favoriteAlbumService()
        .update(this.favoriteAlbum)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          this.alertService().show('info', this, 'mydkApp.favoriteAlbum.updated', {
            param: this.favoriteAlbum.album ? this.favoriteAlbum.album.artistName + ' · ' + this.favoriteAlbum.album.name : param.id,
          });
        })
        .catch(error => {
          this.isSaving = false;
          this.alertService().showHttpError(this, error.response);
        });
    } else {
      this.favoriteAlbumService()
        .create(this.favoriteAlbum)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          this.alertService().show('success', this, 'mydkApp.favoriteAlbum.created', {
            param: this.favoriteAlbum.album ? this.favoriteAlbum.album.artistName + ' · ' + this.favoriteAlbum.album.name : param.id,
          });
        })
        .catch(error => {
          this.isSaving = false;
          this.alertService().showHttpError(this, error.response);
        });
    }
  }
  public retrieveFavoriteAlbum(favoriteAlbumId): void {
    this.favoriteAlbumService()
      .find(favoriteAlbumId)
      .then(res => {
        this.favoriteAlbum = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }
  public previousState(): void {
    this.$router.go(-1);
  }
  public prepareRemove(): void {
    this.removeId = this.favoriteAlbum.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }
  public removeFavoriteAlbum(): void {
    this.favoriteAlbumService()
      .delete(this.removeId)
      .then(() => {
        this.alertService().show('danger', this, 'mydkApp.favoriteAlbum.deleted', {
          param: this.favoriteAlbum.album ? this.favoriteAlbum.album.artistName + ' · ' + this.favoriteAlbum.album.name : this.removeId,
        });
        this.removeId = null;
        this.closeDialog();
        this.$router.go(-1);
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }
  public closeDialog(): void {
    (<any>this.$refs.removeEntity).hide();
  }
}
