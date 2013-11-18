package controllers

import play.api.mvc._
import repository.FacewallRepo
import facade.OverviewFacade
import facade.SearchFacade
import play.api.templates.Html
import controllers.requestmapper.QueryMapper
import facade.modelmapper.{PersonDetailsModelMapper, SearchResultsModelMapper}
import model.{PersonDetailsModel, DefaultSearchResultsModel}

object FacewallController extends Controller {
    val repo = new FacewallRepo()
    val overviewFacade = new OverviewFacade(repo)
    val searchFacade = new SearchFacade(repo, new SearchResultsModelMapper(), new PersonDetailsModelMapper())
    val queryMapper = new QueryMapper()

    def overview = Action {
        val view: Html = views.html.overview(overviewFacade.createOverviewModel)
        Ok(view)
    }

    def search = Action {
        val view: Html = views.html.search()
        Ok(view)
    }

    def searchResults = Action { request: Request[AnyContent] =>
        val query = queryMapper.map(request)
        val view = searchFacade.createSearchResultsModel(query) match {
            case searchResults: DefaultSearchResultsModel => views.html.searchresults(searchResults)
            case personDetails: PersonDetailsModel => views.html.persondetails(personDetails)
        }
        Ok(view)
    }

    def form = Action {
        val view: Html = views.html.form()
        Ok(view)
    }
}