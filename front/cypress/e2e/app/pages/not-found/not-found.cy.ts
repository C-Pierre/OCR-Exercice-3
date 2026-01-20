describe('NotFound page', () => {

  it('should display Not Found page when navigating to unknown route', () => {
    cy.visit('/this-route-does-not-exist', { failOnStatusCode: false })

    cy.url().should('include', '/404')

    cy.get('h1')
      .should('be.visible')
      .and('have.text', 'Page not found !')

    cy.get('h1').should('have.length', 1)

    cy.get('h1').should('have.text', 'Page not found !')
  })

})
