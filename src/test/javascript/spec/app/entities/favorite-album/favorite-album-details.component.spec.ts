/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import FavoriteAlbumDetailComponent from '@/entities/favorite-album/favorite-album-details.vue';
import FavoriteAlbumClass from '@/entities/favorite-album/favorite-album-details.component';
import FavoriteAlbumService from '@/entities/favorite-album/favorite-album.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('FavoriteAlbum Management Detail Component', () => {
    let wrapper: Wrapper<FavoriteAlbumClass>;
    let comp: FavoriteAlbumClass;
    let favoriteAlbumServiceStub: SinonStubbedInstance<FavoriteAlbumService>;

    beforeEach(() => {
      favoriteAlbumServiceStub = sinon.createStubInstance<FavoriteAlbumService>(FavoriteAlbumService);

      wrapper = shallowMount<FavoriteAlbumClass>(FavoriteAlbumDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { favoriteAlbumService: () => favoriteAlbumServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundFavoriteAlbum = { id: 123 };
        favoriteAlbumServiceStub.find.resolves(foundFavoriteAlbum);

        // WHEN
        comp.retrieveFavoriteAlbum(123);
        await comp.$nextTick();

        // THEN
        expect(comp.favoriteAlbum).toBe(foundFavoriteAlbum);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundFavoriteAlbum = { id: 123 };
        favoriteAlbumServiceStub.find.resolves(foundFavoriteAlbum);

        // WHEN
        comp.beforeRouteEnter({ params: { favoriteAlbumId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.favoriteAlbum).toBe(foundFavoriteAlbum);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        comp.previousState();
        await comp.$nextTick();

        expect(comp.$router.currentRoute.fullPath).toContain('/');
      });
    });
  });
});
