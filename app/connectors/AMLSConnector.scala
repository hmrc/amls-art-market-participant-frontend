package connectors

import models.UserAnswers

class AMLSConnector {

  //TODO get mongo cache entry from AMLS FE (id may need to be the CredID from auth)
  def get(id: String) = ???

  //TODO put mongo cache => AMLS FE (id may need to be the CredID from auth)
  def set(id: String, userAnswers: UserAnswers) = ???

}
