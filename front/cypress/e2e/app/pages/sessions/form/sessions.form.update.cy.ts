import { sessionsFixtures } from '../../../../../common/fixtures/sessions.fixtures.cy.ts';

describe('Session Update page', () => {

  const session = sessionsFixtures[2];

  beforeEach(() => {
    cy.loginAndStoreSession()
  })

  it('Should disable submit and show errors when fields are invalid', () => {
    cy.visit('/sessions');

    cy.contains('.mat-mdc-card-header-text', session.name)
    .parents('.mat-mdc-card.mdc-card.item')
    .find('button')
    .contains('Edit')
    .click()

    cy.get('input[formControlName=name]').clear()
    cy.get('button[type=submit]').should('be.disabled')
    cy.get('input[formControlName=name]').type(session.name)

    cy.get('input[formControlName=date]').clear()
    cy.get('button[type=submit]').should('be.disabled')
    cy.get('input[formControlName=date]').type(session.date)

    cy.get('textarea[formControlName=description]').clear()
    cy.get('button[type=submit]').should('be.disabled')
    cy.get('textarea[formControlName=description]').type(session.descrition)
    cy.get('button[type=submit]').should('not.be.disabled')
  })

  it('Should commplete and submit when valid then go back and see new session in list', () => {
    cy.visit('/sessions');

    cy.contains('.mat-mdc-card-header-text', session.name)
    .parents('.mat-mdc-card.mdc-card.item')
    .find('button')
    .contains('Edit')
    .click()

    cy.get('input[formControlName=name]').type(session.name + '_update')
    cy.get('input[formControlName=date]').type(session.date)
    cy.get('[formControlName=teacherId]').click()
    cy.get('#mat-option-1').click()
    cy.get('textarea[formControlName=description]').type(session.descrition + '_update')
    cy.get('button[type=submit]').click()
    cy.url().should('include', '/sessions')
    cy.contains(session.name)
  })

  it('Should access detail new session and delete it then go back', () => {
    cy.contains('.mat-mdc-card-header-text', session.name + '_update')
    .parents('.mat-mdc-card.mdc-card.item')
    .find('button')
    .contains('Detail')
    .click()

    cy.contains('button', 'Delete').click()
    cy.url().should('include', '/sessions')
  })
})
