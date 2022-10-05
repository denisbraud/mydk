import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IFavoriteAlbum } from '@/shared/model/favorite-album.model';

import FavoriteAlbumService from './favorite-album.service';
import AlertService from '@/shared/alert/alert.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class FavoriteAlbum extends Vue {
  @Inject('favoriteAlbumService') private favoriteAlbumService: () => FavoriteAlbumService;
  @Inject('alertService') private alertService: () => AlertService;

  private removeId: number = null;
  public itemsPerPage = 20;
  public queryCount: number = null;
  public page = 1;
  public previousPage = 1;
  public propOrder = 'id';
  public reverse = false;
  public totalItems = 0;

  public favoriteAlbums: IFavoriteAlbum[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllFavoriteAlbums();
  }

  public clear(): void {
    this.page = 1;
    this.retrieveAllFavoriteAlbums();
  }

  public retrieveAllFavoriteAlbums(): void {
    this.isFetching = true;
    const paginationQuery = {
      page: this.page - 1,
      size: this.itemsPerPage,
      sort: this.sort(),
    };
    this.favoriteAlbumService()
      .retrieve(paginationQuery)
      .then(
        res => {
          this.favoriteAlbums = res.data;
          this.totalItems = Number(res.headers['x-total-count']);
          this.queryCount = this.totalItems;
          this.isFetching = false;
        },
        err => {
          this.isFetching = false;
          this.alertService().showHttpError(this, err.response);
        }
      );
  }

  public handleSyncList(): void {
    this.clear();
  }

  public prepareRemove(instance: IFavoriteAlbum): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeFavoriteAlbum(): void {
    this.favoriteAlbumService()
      .delete(this.removeId)
      .then(() => {
        this.alertService().showInfo(this, 'mydkApp.favoriteAlbum.deleted', { param: this.removeId });
        this.removeId = null;
        this.retrieveAllFavoriteAlbums();
        this.closeDialog();
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public sort(): Array<any> {
    const result = [this.propOrder + ',' + (this.reverse ? 'desc' : 'asc')];
    if (this.propOrder !== 'id') {
      result.push('id');
    }
    return result;
  }

  public loadPage(page: number): void {
    if (page !== this.previousPage) {
      this.previousPage = page;
      this.transition();
    }
  }

  public transition(): void {
    this.retrieveAllFavoriteAlbums();
  }

  public changeOrder(propOrder): void {
    this.propOrder = propOrder;
    this.reverse = !this.reverse;
    this.transition();
  }

  public closeDialog(): void {
    (<any>this.$refs.removeEntity).hide();
  }
}
