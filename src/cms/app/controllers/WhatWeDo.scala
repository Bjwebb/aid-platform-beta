package controllers

import play.api.mvc.Controller
import traits.Secured
import com.google.inject.Inject
import uk.gov.dfid.common.api.Api
import models.WhatWeDoEntry
import concurrent.ExecutionContext.Implicits.global

class WhatWeDo @Inject()(val api: Api[WhatWeDoEntry]) extends Controller with Secured {

  def index = SecuredAction { user => request =>
    Async {
      api.all.map { entries =>
        Ok(views.html.whatwedo.index(entries))
      }
    }
  }

  def create = SecuredAction { user => request =>
    Ok(views.html.whatwedo.view(None, WhatWeDoEntry.form))
  }

  def save = SecuredAction { user => implicit request =>
    WhatWeDoEntry.form.bindFromRequest.fold(
      errors => BadRequest(views.html.whatwedo.view(None, errors)),
      entry => {
        api.insert(entry)
        Redirect(routes.WhatWeDo.index())
      }
    )
  }

  def edit(id: String) = SecuredAction { user => implicit request =>
    Async {
      api.get(id).map { maybeWhatWeDo =>
        maybeWhatWeDo.map { whatwedo =>
          val form = WhatWeDoEntry.form.fill(whatwedo)
          Ok(views.html.whatwedo.view(Some(id), form))
        } getOrElse {
          Redirect(routes.WhatWeDo.index())
        }
      }
    }
  }

  def update(id: String) = SecuredAction { user => implicit request =>
    WhatWeDoEntry.form.bindFromRequest.fold(
      errors => BadRequest(views.html.whatwedo.view(Some(id), errors)),
      entry => {
        api.update(id, entry)
        Redirect(routes.WhatWeDo.index)
      }
    )
  }

  def delete(id: String) = SecuredAction { user => implicit request =>
    api.delete(id)
    Redirect(routes.WhatWeDo.index)
  }
}
