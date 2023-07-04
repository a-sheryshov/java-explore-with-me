package ru.practicum.ewm.stats.service.model;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.ewm.stats.dto.StatDto;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "hits")
@Setter
@Getter
@NamedNativeQueries({
        @NamedNativeQuery(name = "GetNotUniqueIpStat", resultSetMapping = "HitToStatDtoMapping",
                query = "select a.app_name as app, h.uri, count(h.ip) as hits " +
                        "from hits as h join apps a on h.app_id = a.app_id " +
                        "where (h.timeStamp between :start and :end) " +
                        "and (h.uri in :uris) " +
                        "group by h.uri, a.app_name order by hits desc "
        ),
        @NamedNativeQuery(name = "GetUniqueIpStat", resultSetMapping = "HitToStatDtoMapping",
                query = "select a.app_name as app, h.uri, count(distinct h.ip) as hits " +
                        "from hits as h join apps a on h.app_id = a.app_id " +
                        "where (h.timeStamp between :start and :end) " +
                        "and (h.uri in :uris) " +
                        "group by h.uri, a.app_name order by hits desc "
        ),
        @NamedNativeQuery(name = "GetNotUniqueIpStatNoUri", resultSetMapping = "HitToStatDtoMapping",
                query = "select a.app_name as app, h.uri, count(h.ip) as hits " +
                        "from hits as h join apps a on h.app_id = a.app_id " +
                        "where (h.timeStamp between :start and :end) " +
                        "group by h.uri, a.app_name order by hits desc "
        ),
        @NamedNativeQuery(name = "GetUniqueIpStatNoUri", resultSetMapping = "HitToStatDtoMapping",
                query = "select a.app_name as app, h.uri, count(distinct h.ip) as hits " +
                        "from hits as h join apps a on h.app_id = a.app_id " +
                        "where (h.timeStamp between :start and :end) " +
                        "group by h.uri, a.app_name order by hits desc "
        )
})
@SqlResultSetMapping(name = "HitToStatDtoMapping",
        classes = {
                @ConstructorResult(
                        targetClass = StatDto.class,
                        columns = {
                                @ColumnResult(name = "app", type = String.class),
                                @ColumnResult(name = "uri", type = String.class),
                                @ColumnResult(name = "hits", type = Integer.class)
                        }
                )}
)
public class Hit {
    @Id
    @Column(name = "hit_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "app_id")
    private App app;
    @Column(name = "uri")
    @Size(max = 100, message = "Should be less than 100 symbols")
    private String uri;
    @Column(name = "ip")
    @Size(max = 50, message = "Should be less than 50 symbols")
    private String ip;
    @Column(name = "timestamp")
    private LocalDateTime timeStamp;
}
