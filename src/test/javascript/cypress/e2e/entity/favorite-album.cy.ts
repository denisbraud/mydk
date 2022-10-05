import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('FavoriteAlbum e2e test', () => {
  const favoriteAlbumPageUrl = '/favorite-album';
  const favoriteAlbumPageUrlPattern = new RegExp('/favorite-album(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const favoriteAlbumSample = { login: 'admin', albumSpotifyId: '1Td5bSMxDrTIDAvxJQIo5t', rank: 1 };

  let favoriteAlbum;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/favorite-albums+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/favorite-albums').as('postEntityRequest');
    cy.intercept('DELETE', '/api/favorite-albums/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (favoriteAlbum) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/favorite-albums/${favoriteAlbum.id}`,
      }).then(() => {
        favoriteAlbum = undefined;
      });
    }
  });

  it('FavoriteAlbums menu should load FavoriteAlbums page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('favorite-album');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
      cy.screenshot('favorite-album');
    });
    cy.getEntityHeading('FavoriteAlbum').should('exist');
    cy.url().should('match', favoriteAlbumPageUrlPattern);
  });

  describe('FavoriteAlbum page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(favoriteAlbumPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create FavoriteAlbum page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/favorite-album/new$'));
        cy.getEntityCreateUpdateHeading('FavoriteAlbum');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode ?? -1).to.equal(200);
        });
        cy.url().should('match', favoriteAlbumPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/favorite-albums',
          body: favoriteAlbumSample,
        }).then(({ body }) => {
          favoriteAlbum = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/favorite-albums+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/favorite-albums?page=0&size=20>; rel="last",<http://localhost/api/favorite-albums?page=0&size=20>; rel="first"',
              },
              body: [favoriteAlbum],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(favoriteAlbumPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details FavoriteAlbum page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('favoriteAlbum');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode ?? -1).to.equal(200);
        });
        cy.url().should('match', favoriteAlbumPageUrlPattern);
      });

      it('edit button click should load edit FavoriteAlbum page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FavoriteAlbum');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode ?? -1).to.equal(200);
        });
        cy.url().should('match', favoriteAlbumPageUrlPattern);
      });

      it('edit button click should load edit FavoriteAlbum page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FavoriteAlbum');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode ?? -1).to.equal(200);
        });
        cy.url().should('match', favoriteAlbumPageUrlPattern);
      });

      it('last delete button click should delete instance of FavoriteAlbum', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('favoriteAlbum').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode ?? -1).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode ?? -1).to.equal(200);
        });
        cy.url().should('match', favoriteAlbumPageUrlPattern);

        favoriteAlbum = undefined;
      });
    });
  });

  describe('new FavoriteAlbum page', () => {
    beforeEach(() => {
      cy.visit(`${favoriteAlbumPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('FavoriteAlbum');
    });

    it('should create an instance of FavoriteAlbum', () => {
      cy.get(`[data-cy="login"]`).type('admin');
      //.should('have.value', 'admin'); commented because field became invisible

      cy.get(`[data-cy="albumSpotifyId"]`).type('1Td5bSMxDrTIDAvxJQIo5t');
      //.should('have.value', '1Td5bSMxDrTIDAvxJQIo5t');  commented because field became invisible

      cy.get(`[data-cy="rank"]`).type('1').should('have.value', '1');

      cy.get(`[data-cy="comment"]`).type('Ceci est un commentaire').should('have.value', 'Ceci est un commentaire');

      cy.screenshot('favorite-album-update');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode ?? -1).to.equal(201);
        favoriteAlbum = response?.body ?? null;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode ?? -1).to.equal(200);
      });
      cy.url().should('match', favoriteAlbumPageUrlPattern);
    });
  });
});
