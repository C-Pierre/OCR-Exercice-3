import { sessionsFixtures } from '../../../../../common/fixtures/sessions.fixtures.cy.ts';

describe('Session Create page', () => {

  const session = sessionsFixtures[2];

  beforeEach(() => {
    cy.loginAndStoreSession()
    cy.visit('/sessions/create');
  })

  it('Should disable submit and show errors when fields are invalid', () => {
    cy.get('button[type=submit]').should('be.disabled')

    cy.get('input[formControlName=name]').type(session.name)
    cy.get('button[type=submit]').should('be.disabled')

    cy.get('input[formControlName=date]').type(session.date)
    cy.get('button[type=submit]').should('be.disabled')

    cy.get('[formControlName=teacherId]').click()
    cy.get('#mat-option-0').click()
    cy.get('button[type=submit]').should('be.disabled')

    cy.get('textarea[formControlName=description]').type(session.descrition)
    cy.get('button[type=submit]').should('not.be.disabled')
  })

  it('Should commplete and submit when valid then go back and see new session in list', () => {
    cy.get('input[formControlName=name]').type(session.name)
    cy.get('input[formControlName=date]').type(session.date)
    cy.get('[formControlName=teacherId]').click()
    cy.get('#mat-option-0').click()
    cy.get('textarea[formControlName=description]').type(session.descrition)
    cy.get('button[type=submit]').click()
    cy.url().should('include', '/sessions')
    cy.contains(session.name)
  })
})
