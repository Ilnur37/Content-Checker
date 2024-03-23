package edu.java.scrapper.service.jpa;

import edu.java.models.dto.request.AddLinkRequest;
import edu.java.models.dto.request.RemoveLinkRequest;
import edu.java.models.dto.response.LinkResponse;
import edu.java.models.dto.response.ListLinksResponse;
import edu.java.models.exception.ChatIdNotFoundException;
import edu.java.models.exception.LinkNotFoundException;
import edu.java.models.exception.ReAddLinkException;
import edu.java.scrapper.domain.jpa.dao.JpaChatRepository;
import edu.java.scrapper.domain.jpa.dao.JpaLinkRepository;
import edu.java.scrapper.domain.jpa.model.Chat;
import edu.java.scrapper.domain.jpa.model.Link;
import edu.java.scrapper.dto.github.RepositoryInfo;
import edu.java.scrapper.dto.stackoverflow.question.QuestionInfo;
import edu.java.scrapper.service.LinkService;
import edu.java.scrapper.service.web.WebResourceHandler;
import jakarta.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
public class JpaLinkService implements LinkService {

    private final JpaChatRepository chatRepository;
    private final JpaLinkRepository linkRepository;
    private final WebResourceHandler webResourceHandler;

    @Override
    public ListLinksResponse getAll(long tgChatId) {
        Chat chat = getChatByTgChatId(tgChatId);

        List<LinkResponse> linkResponses = chat.getLinks()
            .stream()
            .map(row -> new LinkResponse(row.getId(), row.getUrl()))
            .toList();

        return new ListLinksResponse(linkResponses, linkResponses.size());
    }

    @Override
    public LinkResponse add(long tgChatId, AddLinkRequest linkRequest) {
        String url = linkRequest.link();
        Chat chat = getChatByTgChatId(tgChatId);
        String author = EMPTY_STRING;
        String title = EMPTY_STRING;

        if (webResourceHandler.isGitHubUrl(url)) {
            RepositoryInfo repositoryInfo = webResourceHandler.getRepositoryGitHubInfoByUrl(url);
            author = repositoryInfo.getActor().getLogin();
            title = repositoryInfo.getName();
        } else if (webResourceHandler.isStackOverflowUrl(url)) {
            QuestionInfo questionInfo = webResourceHandler.getQuestionStackOverflowByUrl(url);
            author = questionInfo.getOwner().getDisplayName();
            title = questionInfo.getTitle();
        }

        Link actualLink;
        //Создание ссылки в таблице ссылок, если ее нет
        if (linkRepository.findLinkByUrl(url).isEmpty()) {
            OffsetDateTime nowTime = OffsetDateTime.now();
            Link createLink = createLink(url, nowTime, nowTime, author, title, nowTime);
            linkRepository.save(createLink);
            actualLink = linkRepository.findLinkByUrl(url).get();
        } else {
            //Иначе проверка на предмет повторного добавления
            actualLink = linkRepository.findLinkByUrl(url).get();
            for (Link link : chat.getLinks()) {
                if (link.getUrl().equals(actualLink.getUrl())) {
                    throw new ReAddLinkException(
                        toExMsg(EX_CHAT, String.valueOf(tgChatId))
                            + ", "
                            + toExMsg(EX_LINK, actualLink.getUrl())
                    );
                }
            }
        }

        chat.addLink(actualLink);

        return new LinkResponse(actualLink.getId(), actualLink.getUrl());
    }

    @Override
    public LinkResponse remove(long tgChatId, RemoveLinkRequest linkRequest) {
        Chat chat = getChatByTgChatId(tgChatId);
        String url = linkRequest.link();
        Link actualLink = getLinkByUrl(url);
        chat.removeLink(actualLink);

        return new LinkResponse(actualLink.getId(), actualLink.getUrl());
    }

    private Chat getChatByTgChatId(long tgChatId) {
        return chatRepository.findChatWithLinkByTgChatId(tgChatId)
            .orElseThrow(
                () -> new ChatIdNotFoundException(toExMsg(EX_CHAT, String.valueOf(tgChatId)))
            );
    }

    private Link getLinkByUrl(String url) {
        return linkRepository.findLinkWithChatByUrl(url)
            .orElseThrow(
                () -> new LinkNotFoundException(toExMsg(EX_LINK, url))
            );
    }

    private Link createLink(
        String url,
        OffsetDateTime createdAt,
        OffsetDateTime lastUpdateAt,
        String name,
        String author,
        OffsetDateTime lastCheckAt
    ) {
        Link link = new Link();
        link.setUrl(url);
        link.setCreatedAt(createdAt);
        link.setLastUpdateAt(lastUpdateAt);
        link.setName(name);
        link.setAuthor(author);
        link.setLastCheckAt(lastCheckAt);
        return link;
    }
}
