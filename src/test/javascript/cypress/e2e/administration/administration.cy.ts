import {
  userManagementPageHeadingSelector,
  metricsPageHeadingSelector,
  healthPageHeadingSelector,
  logsPageHeadingSelector,
  configurationPageHeadingSelector,
  swaggerPageSelector,
  swaggerFrameSelector,
} from '../../support/commands';

describe('/admin', () => {
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';

  beforeEach(() => {
    cy.login(username, password);
    cy.visit('');
  });

  describe('/user-management', () => {
    it('should load the page', () => {
      cy.screenshot('home');
      cy.clickOnAdminMenuItem('user-management');
      cy.get(userManagementPageHeadingSelector).should('be.visible');
      cy.screenshot('user-management');
    });
  });

  describe('/metrics', () => {
    it('should load the page', () => {
      cy.clickOnAdminMenuItem('metrics');
      cy.get(metricsPageHeadingSelector).should('be.visible');
      cy.screenshot('metrics');
    });
  });

  describe('/health', () => {
    it('should load the page', () => {
      cy.clickOnAdminMenuItem('health');
      cy.get(healthPageHeadingSelector).should('be.visible');
      cy.screenshot('health');
    });
  });

  describe('/logs', () => {
    it('should load the page', () => {
      cy.clickOnAdminMenuItem('logs');
      cy.get(logsPageHeadingSelector).should('be.visible');
      //cy.screenshot('logs'); ko avec infinity span
    });
  });

  describe('/configuration', () => {
    it('should load the page', () => {
      cy.clickOnAdminMenuItem('configuration');
      cy.get(configurationPageHeadingSelector).should('be.visible');
      cy.screenshot('configuration');
    });
  });

  describe('/docs', () => {
    it('should load the page', () => {
      cy.getManagementInfo().then(info => {
        if (info.activeProfiles.includes('api-docs')) {
          cy.clickOnAdminMenuItem('docs');
          cy.get(swaggerFrameSelector)
            .should('be.visible')
            .then(() => {
              // Wait iframe to load
              cy.wait(500); // eslint-disable-line cypress/no-unnecessary-waiting
              cy.get(swaggerFrameSelector).its('0.contentDocument.body').find(swaggerPageSelector, { timeout: 15000 }).should('be.visible');
              //cy.screenshot('docs'); ko : see https://github.com/cypress-io/cypress/issues/15619
            });
        }
      });
    });
  });
});
