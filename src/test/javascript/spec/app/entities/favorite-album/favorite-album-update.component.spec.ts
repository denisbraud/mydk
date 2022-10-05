/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import FavoriteAlbumUpdateComponent from '@/entities/favorite-album/favorite-album-update.vue';
import FavoriteAlbumClass from '@/entities/favorite-album/favorite-album-update.component';
import FavoriteAlbumService from '@/entities/favorite-album/favorite-album.service';

import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
const router = new Router();
localVue.use(Router);
localVue.use(ToastPlugin);
localVue.component('font-awesome-icon', {});
localVue.component('b-input-group', {});
localVue.component('b-input-group-prepend', {});
localVue.component('b-form-datepicker', {});
localVue.component('b-form-input', {});

describe('Component Tests', () => {
  describe('FavoriteAlbum Management Update Component', () => {
    let wrapper: Wrapper<FavoriteAlbumClass>;
    let comp: FavoriteAlbumClass;
    let favoriteAlbumServiceStub: SinonStubbedInstance<FavoriteAlbumService>;

    beforeEach(() => {
      favoriteAlbumServiceStub = sinon.createStubInstance<FavoriteAlbumService>(FavoriteAlbumService);

      wrapper = shallowMount<FavoriteAlbumClass>(FavoriteAlbumUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          favoriteAlbumService: () => favoriteAlbumServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.favoriteAlbum = entity;
        favoriteAlbumServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(favoriteAlbumServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.favoriteAlbum = entity;
        favoriteAlbumServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(favoriteAlbumServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundFavoriteAlbum = { id: 123 };
        favoriteAlbumServiceStub.find.resolves(foundFavoriteAlbum);
        favoriteAlbumServiceStub.retrieve.resolves([foundFavoriteAlbum]);

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
