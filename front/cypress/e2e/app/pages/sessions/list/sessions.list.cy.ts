import { sessionsFixtures } from '../../../../../common/fixtures/sessions.fixtures.cy.ts';

describe('Sessions List page', () => {

  const session = sessionsFixtures[0];

  beforeEach(() => {
    cy.loginAndStoreSession()
    cy.visit('/sessions');
  })

  it('Should display sessions information', () => {
    cy.contains('Sessions available')
    cy.contains(session.name)
  })

  it('Should acces to create session form and go back', () => {
    cy.contains('button', 'Create').click()
    cy.url().should('include', '/sessions/create')
    cy.contains("Create session")
    cy.contains('button', 'arrow_back').click()
    cy.url().should('include', '/sessions')
  })

  it('Should acces to detail session go back', () => {
    cy.contains('button', 'Detail').click()
    cy.url().should('include', '/sessions/detail/' + session.id)
    cy.contains(session.name)
    cy.contains('button', 'arrow_back').click()
    cy.url().should('include', '/sessions')
  })

  it('Should acces to edit session go back', () => {
    cy.contains('button', 'Edit').click()
    cy.url().should('include', '/sessions/update/' + session.id)
    cy.contains('Update session')
    cy.contains('button', 'Save')
    cy.contains('button', 'arrow_back').click()
    cy.url().should('include', '/sessions')
  })
})
