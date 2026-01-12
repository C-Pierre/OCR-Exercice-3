import { sessionsFixtures } from '../../../../../common/fixtures/sessions.fixtures.cy.ts';
import { userFixtures } from '../../../../../common/fixtures/user.fixtures.cy.ts';

describe('Session Detail page', () => {

  const session = sessionsFixtures[0];

  beforeEach(() => {
  })

  it('Should show see infos and delete button for admin', () => {
    cy.loginAndStoreSession()
    cy.visit(`/sessions/detail/${session.id}`);
    cy.contains(session.name)
    cy.contains(session.descrition)
    cy.contains('button', 'Delete').should('be.visible')
  })

  it('Should visit participate and go back', () => {
    cy.loginAndStoreSession(userFixtures)
    cy.visit(`/sessions/detail/${session.id}`);
    cy.contains('button', 'Participate').click()
    cy.contains('button', 'Do not participate')
    cy.url().should('include', '/sessions')
  })

  it('Should visit no longer participate and go back', () => {
    cy.loginAndStoreSession(userFixtures)
    cy.visit(`/sessions/detail/${session.id}`);
    cy.contains('button', 'Do not participate').click()
    cy.contains('button', 'Participate')
    cy.url().should('include', '/sessions')
  })
})
