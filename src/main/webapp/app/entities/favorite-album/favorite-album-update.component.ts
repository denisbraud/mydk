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

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.favoriteAlbumId) {
        vm.retrieveFavoriteAlbum(to.params.favoriteAlbumId);
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
          const message = this.$t('mydkApp.favoriteAlbum.updated', { param: param.id });
          return this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Info',
            variant: 'info',
            solid: true,
            autoHideDelay: 5000,
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
          const message = this.$t('mydkApp.favoriteAlbum.created', { param: param.id });
          this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Success',
            variant: 'success',
            solid: true,
            autoHideDelay: 5000,
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
}
