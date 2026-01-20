import { userFixtures } from '../../../../common/fixtures/user.fixtures.cy.ts';

describe('Account Detail page', () => {

  beforeEach(() => {
    cy.loginAndStoreSession(userFixtures)
  })

  it('Me - Should swho account page and see username and go back', () => {
    cy.visit('/me');
    cy.contains(userFixtures.firstName)
    cy.contains(userFixtures.lastName)
    cy.contains(userFixtures.username)
    cy.contains('button', 'arrow_back').click()
    cy.url().should('include', '/sessions')
  })
})
