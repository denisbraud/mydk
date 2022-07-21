import Component from 'vue-class-component';
import { Inject, Vue } from 'vue-property-decorator';
import LoginService from '@/account/login.service';
import SpotifyService from './spotify.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class Home extends Vue {
  @Inject('loginService') private loginService: () => LoginService;
  @Inject('spotifyService') private spotifyService: () => SpotifyService;
  @Inject('alertService') private alertService: () => AlertService;
  public artistName = '';
  public albumName = '';
  public mainAlbumOnly = true;
  public albums: any[] = [];
  public isLoading = false;

  beforeRouteEnter(to, from, next) {
    next(vm => {
      vm.loadFavorite();
    });
  }
  public openLogin(): void {
    this.loginService().openLogin((<any>this).$root);
  }
  public get authenticated(): boolean {
    return this.$store.getters.authenticated;
  }
  public get username(): string {
    return this.$store.getters.account?.login ?? '';
  }
  public loadFavorite(): void {
    this.artistName = '';
    this.albumName = '';
    this.search();
  }
  public search(): void {
    this.isLoading = true;
    this.spotifyService()
      .search(this.artistName, this.albumName, this.mainAlbumOnly)
      .then(res => {
        this.isLoading = false;
        this.albums = res.data;
      })
      .catch(error => {
        this.isLoading = false;
        this.alertService().showHttpError(this, error.response);
      });
  }
}
