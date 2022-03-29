package tk.maizbagwala.social.model

data class ReelModel(
    val graphql: Graphql
) {
    data class Graphql(
        val shortcode_media: ShortcodeMedia
    ) {
        data class ShortcodeMedia(
            val __typename: String,
            val accessibility_caption: Any,
            val can_see_insights_as_brand: Boolean,
            val caption_is_edited: Boolean,
            val clips_music_attribution_info: ClipsMusicAttributionInfo,
            val coauthor_producers: List<Any>,
            val commenting_disabled_for_viewer: Boolean,
            val comments_disabled: Boolean,
            val dash_info: DashInfo,
            val dimensions: Dimensions,
            val display_resources: List<DisplayResource>,
            val display_url: String,
            val edge_media_preview_comment: EdgeMediaPreviewComment,
            val edge_media_preview_like: EdgeMediaPreviewLike,
            val edge_media_to_caption: EdgeMediaToCaption,
            val edge_media_to_hoisted_comment: EdgeMediaToHoistedComment,
            val edge_media_to_parent_comment: EdgeMediaToParentComment,
            val edge_media_to_sponsor_user: EdgeMediaToSponsorUser,
            val edge_media_to_tagged_user: EdgeMediaToTaggedUser,
            val edge_related_profiles: EdgeRelatedProfiles,
            val edge_web_media_to_related_media: EdgeWebMediaToRelatedMedia,
            val encoding_status: Any,
            val fact_check_information: Any,
            val fact_check_overall_rating: Any,
            val gating_info: Any,
            val has_audio: Boolean,
            val has_ranked_comments: Boolean,
            val id: String,
            val is_ad: Boolean,
            val is_affiliate: Boolean,
            val is_paid_partnership: Boolean,
            val is_published: Boolean,
            val is_video: Boolean,
            val like_and_view_counts_disabled: Boolean,
            val location: Any,
            val media_overlay_info: Any,
            val media_preview: String,
            val owner: Owner,
            val product_type: String,
            val sensitivity_friction_info: Any,
            val sharing_friction_info: SharingFrictionInfo,
            val shortcode: String,
            val taken_at_timestamp: Int,
            val thumbnail_src: String,
            val title: String,
            val tracking_token: String,
            val upcoming_event: Any,
            val video_duration: Double,
            val video_play_count: Int,
            val video_url: String,
            val video_view_count: Int,
            val viewer_can_reshare: Boolean,
            val viewer_has_liked: Boolean,
            val viewer_has_saved: Boolean,
            val viewer_has_saved_to_collection: Boolean,
            val viewer_in_photo_of_you: Boolean
        ) {
            data class ClipsMusicAttributionInfo(
                val artist_name: String,
                val audio_id: String,
                val should_mute_audio: Boolean,
                val should_mute_audio_reason: String,
                val song_name: String,
                val uses_original_audio: Boolean
            )

            data class DashInfo(
                val is_dash_eligible: Boolean,
                val number_of_qualities: Int,
                val video_dash_manifest: Any
            )

            data class Dimensions(
                val height: Int,
                val width: Int
            )

            data class DisplayResource(
                val config_height: Int,
                val config_width: Int,
                val src: String
            )

            data class EdgeMediaPreviewComment(
                val count: Int,
                val edges: List<Edge>
            ) {
                data class Edge(
                    val node: Node
                ) {
                    data class Node(
                        val created_at: Int,
                        val did_report_as_spam: Boolean,
                        val edge_liked_by: EdgeLikedBy,
                        val id: String,
                        val is_restricted_pending: Boolean,
                        val owner: Owner,
                        val text: String,
                        val viewer_has_liked: Boolean
                    ) {
                        data class EdgeLikedBy(
                            val count: Int
                        )

                        data class Owner(
                            val id: String,
                            val is_verified: Boolean,
                            val profile_pic_url: String,
                            val username: String
                        )
                    }
                }
            }

            data class EdgeMediaPreviewLike(
                val count: Int,
                val edges: List<Any>
            )

            data class EdgeMediaToCaption(
                val edges: List<Edge>
            ) {
                data class Edge(
                    val node: Node
                ) {
                    data class Node(
                        val text: String
                    )
                }
            }

            data class EdgeMediaToHoistedComment(
                val edges: List<Any>
            )

            data class EdgeMediaToParentComment(
                val count: Int,
                val edges: List<Edge>,
                val page_info: PageInfo
            ) {
                data class Edge(
                    val node: Node
                ) {
                    data class Node(
                        val created_at: Int,
                        val did_report_as_spam: Boolean,
                        val edge_liked_by: EdgeLikedBy,
                        val edge_threaded_comments: EdgeThreadedComments,
                        val id: String,
                        val is_restricted_pending: Boolean,
                        val owner: Owner,
                        val text: String,
                        val viewer_has_liked: Boolean
                    ) {
                        data class EdgeLikedBy(
                            val count: Int
                        )

                        data class EdgeThreadedComments(
                            val count: Int,
                            val edges: List<Edge>,
                            val page_info: PageInfo
                        ) {
                            data class Edge(
                                val node: Node
                            ) {
                                data class Node(
                                    val created_at: Int,
                                    val did_report_as_spam: Boolean,
                                    val edge_liked_by: EdgeLikedBy,
                                    val id: String,
                                    val is_restricted_pending: Boolean,
                                    val owner: Owner,
                                    val text: String,
                                    val viewer_has_liked: Boolean
                                ) {
                                    data class EdgeLikedBy(
                                        val count: Int
                                    )

                                    data class Owner(
                                        val id: String,
                                        val is_verified: Boolean,
                                        val profile_pic_url: String,
                                        val username: String
                                    )
                                }
                            }

                            data class PageInfo(
                                val end_cursor: Any,
                                val has_next_page: Boolean
                            )
                        }

                        data class Owner(
                            val id: String,
                            val is_verified: Boolean,
                            val profile_pic_url: String,
                            val username: String
                        )
                    }
                }

                data class PageInfo(
                    val end_cursor: String,
                    val has_next_page: Boolean
                )
            }

            data class EdgeMediaToSponsorUser(
                val edges: List<Any>
            )

            data class EdgeMediaToTaggedUser(
                val edges: List<Any>
            )

            data class EdgeRelatedProfiles(
                val edges: List<Any>
            )

            data class EdgeWebMediaToRelatedMedia(
                val edges: List<Any>
            )

            data class Owner(
                val blocked_by_viewer: Boolean,
                val edge_followed_by: EdgeFollowedBy,
                val edge_owner_to_timeline_media: EdgeOwnerToTimelineMedia,
                val followed_by_viewer: Boolean,
                val full_name: String,
                val has_blocked_viewer: Boolean,
                val id: String,
                val is_embeds_disabled: Boolean,
                val is_private: Boolean,
                val is_unpublished: Boolean,
                val is_verified: Boolean,
                val pass_tiering_recommendation: Boolean,
                val profile_pic_url: String,
                val requested_by_viewer: Boolean,
                val restricted_by_viewer: Any,
                val username: String
            ) {
                data class EdgeFollowedBy(
                    val count: Int
                )

                data class EdgeOwnerToTimelineMedia(
                    val count: Int
                )
            }

            data class SharingFrictionInfo(
                val bloks_app_url: Any,
                val should_have_sharing_friction: Boolean
            )
        }
    }
}