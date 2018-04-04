--
-- PostgreSQL database dump
--

-- Dumped from database version 9.6.2
-- Dumped by pg_dump version 9.6.2

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: channel; Type: SCHEMA; Schema: -; Owner: -
--

CREATE SCHEMA channel;


--
-- Name: group; Type: SCHEMA; Schema: -; Owner: -
--

CREATE SCHEMA "group";


--
-- Name: profile; Type: SCHEMA; Schema: -; Owner: -
--

CREATE SCHEMA profile;


--
-- Name: source; Type: SCHEMA; Schema: -; Owner: -
--

CREATE SCHEMA source;


--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: -
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


--
-- Name: ltree; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS ltree WITH SCHEMA public;


--
-- Name: EXTENSION ltree; Type: COMMENT; Schema: -; Owner: -
--

COMMENT ON EXTENSION ltree IS 'data type for hierarchical tree-like structures';


SET search_path = channel, pg_catalog;

--
-- Name: f_channel_identifier_delete(bigint); Type: FUNCTION; Schema: channel; Owner: -
--

CREATE FUNCTION f_channel_identifier_delete(v_channel_identifier_id bigint) RETURNS TABLE(succeeded boolean, message text)
    LANGUAGE plpgsql
    AS $$
  BEGIN
    IF EXISTS(
      SELECT 1 FROM profile.channel_identifier_profile cip 
      WHERE v_channel_identifier_id = cip.channel_identifier_id
    )
      THEN RETURN  QUERY
      SELECT FALSE,'channel identifier has profile associations'::TEXT;
    ELSE 
      DELETE  FROM channel.channel_identifier as ci
      WHERE v_channel_identifier_id = ci.channel_identifier_id;
      IF FOUND IS FALSE THEN RETURN  QUERY
        SELECT FALSE,'channel identifier does not exist'::TEXT;
      ELSE RETURN QUERY
        SELECT TRUE ,''::TEXT;
      END IF ;
    END IF ; 
  END;
  $$;


--
-- Name: FUNCTION f_channel_identifier_delete(v_channel_identifier_id bigint); Type: COMMENT; Schema: channel; Owner: -
--

COMMENT ON FUNCTION f_channel_identifier_delete(v_channel_identifier_id bigint) IS 'Deletes a channel identifier';


--
-- Name: f_channel_identifier_insert(integer, text, integer); Type: FUNCTION; Schema: channel; Owner: -
--

CREATE FUNCTION f_channel_identifier_insert(v_channel_id integer, v_identifier text, v_update_user_id integer) RETURNS TABLE(channel_identifier_id bigint, succeeded boolean, message text)
    LANGUAGE plpgsql
    AS $$
    DECLARE v_channel_identifier_id  BIGINT;
  BEGIN
    INSERT  INTO channel.channel_identifier (channel_id, identifier, update_user_id,  update_date)
    VALUES (v_channel_id,v_identifier,v_update_user_id, timezone('utc'::text, now()))
    RETURNING channel_identifier.channel_identifier_id INTO v_channel_identifier_id;
    IF FOUND IS FALSE THEN RETURN  QUERY
      SELECT NULL::BIGINT ,'channel identifier was not created'::TEXT;
    ELSE RETURN QUERY
      SELECT v_channel_identifier_id, TRUE ,''::TEXT;
    END IF ;
  END;
  $$;


--
-- Name: FUNCTION f_channel_identifier_insert(v_channel_id integer, v_identifier text, v_update_user_id integer); Type: COMMENT; Schema: channel; Owner: -
--

COMMENT ON FUNCTION f_channel_identifier_insert(v_channel_id integer, v_identifier text, v_update_user_id integer) IS 'Creates a channel identifier';


--
-- Name: f_channel_identifier_insert(integer, text, integer, integer); Type: FUNCTION; Schema: channel; Owner: -
--

CREATE FUNCTION f_channel_identifier_insert(v_channel_id integer, v_identifier text, v_domain_id integer, v_update_user_id integer) RETURNS TABLE(channel_identifier_id bigint, succeeded boolean, message text)
    LANGUAGE plpgsql
    AS $$
    DECLARE v_channel_identifier_id  BIGINT;
  BEGIN
    INSERT  INTO channel.channel_identifier (channel_id, identifier, update_user_id, domain_id, update_date)
    VALUES (v_channel_id,v_identifier,v_update_user_id,v_domain_id, timezone('utc'::text, now()))
    RETURNING channel_identifier.channel_identifier_id INTO v_channel_identifier_id;
    IF FOUND IS FALSE THEN RETURN  QUERY
      SELECT NULL::BIGINT ,'channel identifier was not created'::TEXT;
    ELSE RETURN QUERY
      SELECT v_channel_identifier_id, TRUE ,''::TEXT;
    END IF ;
  END;
  $$;


--
-- Name: FUNCTION f_channel_identifier_insert(v_channel_id integer, v_identifier text, v_domain_id integer, v_update_user_id integer); Type: COMMENT; Schema: channel; Owner: -
--

COMMENT ON FUNCTION f_channel_identifier_insert(v_channel_id integer, v_identifier text, v_domain_id integer, v_update_user_id integer) IS 'Creates a channel identifier';


--
-- Name: f_channel_identifier_select(bigint); Type: FUNCTION; Schema: channel; Owner: -
--

CREATE FUNCTION f_channel_identifier_select(v_channel_identifier_id bigint) RETURNS TABLE(channel_identifier_id bigint, channel_id integer, identifier text)
    LANGUAGE plpgsql IMMUTABLE
    AS $$
  BEGIN
    RETURN QUERY SELECT
      ci.channel_identifier_id
    , ci.channel_id
    , ci.identifier
    FROM channel.channel_identifier AS ci
      WHERE v_channel_identifier_id=ci.channel_identifier_id;
  END;
  $$;


--
-- Name: FUNCTION f_channel_identifier_select(v_channel_identifier_id bigint); Type: COMMENT; Schema: channel; Owner: -
--

COMMENT ON FUNCTION f_channel_identifier_select(v_channel_identifier_id bigint) IS 'Gets the data for a channel identifier';


--
-- Name: f_channel_identifier_select_by_identifier(integer, text); Type: FUNCTION; Schema: channel; Owner: -
--

CREATE FUNCTION f_channel_identifier_select_by_identifier(v_channel_id integer, v_identifier text) RETURNS TABLE(channel_identifier_id bigint, channel_id integer, identifier text)
    LANGUAGE plpgsql IMMUTABLE
    AS $$
  BEGIN
    RETURN QUERY SELECT
      ci.channel_identifier_id
    , ci.channel_id
    , ci.identifier
    FROM channel.channel_identifier AS ci
      WHERE ci.identifier = v_identifier
      AND ci.channel_id = v_channel_id
    ;
  END;
  $$;


--
-- Name: FUNCTION f_channel_identifier_select_by_identifier(v_channel_id integer, v_identifier text); Type: COMMENT; Schema: channel; Owner: -
--

COMMENT ON FUNCTION f_channel_identifier_select_by_identifier(v_channel_id integer, v_identifier text) IS 'Gets the data for a channel identifier by identifier and channel_id fields';


--
-- Name: f_channel_identifier_update(bigint, integer, text, integer); Type: FUNCTION; Schema: channel; Owner: -
--

CREATE FUNCTION f_channel_identifier_update(v_channel_identifier_id bigint, v_channel_id integer, v_identifier text, v_update_user_id integer) RETURNS TABLE(succeeded boolean, message text)
    LANGUAGE plpgsql
    AS $$
  BEGIN
    UPDATE channel.channel_identifier SET
      channel_id = v_channel_id
    , identifier = v_identifier
    , update_date =timezone('utc'::text, now())
    , update_user_id = v_update_user_id
    WHERE  channel_identifier_id = v_channel_identifier_id;
    IF FOUND IS FALSE THEN RETURN  QUERY
      SELECT FALSE,'channel identifier does not exist'::TEXT;
    ELSE RETURN QUERY
      SELECT TRUE ,''::TEXT;
    END IF ;
  END;
  $$;


--
-- Name: FUNCTION f_channel_identifier_update(v_channel_identifier_id bigint, v_channel_id integer, v_identifier text, v_update_user_id integer); Type: COMMENT; Schema: channel; Owner: -
--

COMMENT ON FUNCTION f_channel_identifier_update(v_channel_identifier_id bigint, v_channel_id integer, v_identifier text, v_update_user_id integer) IS 'Updates a channel identifier';


--
-- Name: f_channel_identifier_update(bigint, integer, text, integer, integer); Type: FUNCTION; Schema: channel; Owner: -
--

CREATE FUNCTION f_channel_identifier_update(v_channel_identifier_id bigint, v_channel_id integer, v_identifier text, v_domain_id integer, v_update_user_id integer) RETURNS TABLE(succeeded boolean, message text)
    LANGUAGE plpgsql
    AS $$
  BEGIN
    UPDATE channel.channel_identifier SET
      channel_id = v_channel_id
    , identifier = v_identifier
    , update_date =timezone('utc'::text, now())
    , update_user_id = v_update_user_id
    , domain_id = v_domain_id
    WHERE  channel_identifier_id = v_channel_identifier_id;
    IF FOUND IS FALSE THEN RETURN  QUERY
      SELECT FALSE,'channel identifier does not exist'::TEXT;
    ELSE RETURN QUERY
      SELECT TRUE ,''::TEXT;
    END IF ;
  END;
  $$;


--
-- Name: FUNCTION f_channel_identifier_update(v_channel_identifier_id bigint, v_channel_id integer, v_identifier text, v_domain_id integer, v_update_user_id integer); Type: COMMENT; Schema: channel; Owner: -
--

COMMENT ON FUNCTION f_channel_identifier_update(v_channel_identifier_id bigint, v_channel_id integer, v_identifier text, v_domain_id integer, v_update_user_id integer) IS 'Updates a channel identifier';


SET search_path = "group", pg_catalog;

--
-- Name: f_group_delete(integer, integer); Type: FUNCTION; Schema: group; Owner: -
--

CREATE FUNCTION f_group_delete(v_group_id integer, v_update_user_id integer) RETURNS TABLE(success boolean, message text)
    LANGUAGE plpgsql
    AS $$
DECLARE
  v_group_type_id "group"."group".group_type_id%TYPE;
BEGIN
  SELECT g.group_type_id
  INTO v_group_type_id
  FROM "group"."group" AS g
  WHERE g.group_id = v_group_id;
  CASE
    WHEN NOT FOUND
    THEN
      RETURN QUERY SELECT
                     FALSE,
                     'group does not exists' :: TEXT;
      --if group is org and has descendants, exit with failure
    WHEN v_group_type_id = 2 --org
         AND (EXISTS(SELECT 1
                     FROM "group".group_profile gp
                     WHERE v_group_id = gp.group_id)
              OR EXISTS(SELECT 1
                        FROM "group".group_relationship gp
                        WHERE v_group_id = gp.group_id))
    THEN
      RETURN QUERY SELECT
                     FALSE,
                     'Cannot delete Org Group with Descendants' :: TEXT;
      --if group is  root, exit with failure
    WHEN v_group_type_id = 1
    THEN --root
      RETURN QUERY SELECT
                     FALSE,
                     'Cannot delete Root group' :: TEXT;
      --validation passed, determine group type
    WHEN v_group_type_id = 3
    THEN --FOR OVERLAY GROUPS, REMOVE FIRST LEVEL ASSOCIATIONS, BOTH CHILD AND PARENT
      --remove all permissions from group
      CREATE TEMP TABLE history_cte (
        group_permission_id INT
      );
      WITH cte AS (
        UPDATE "group".group_permission AS gp
        SET update_user_id = v_update_user_id
          , update_date    = timezone('utc' :: TEXT, now())
        WHERE gp.group_id = v_group_id
        RETURNING gp.group_permission_id
      )
      INSERT INTO history_cte (group_permission_id)
        SELECT cte.group_permission_id
        FROM cte;

      DELETE FROM "group".group_permission AS gp
      USING history_cte
      WHERE gp.group_permission_id = history_cte.group_permission_id;
      DROP TABLE IF EXISTS history_cte;

      -- remove all filters from group
      CREATE TEMP TABLE history_cte (
        group_filter_id INT
      );
      WITH cte AS (
        UPDATE "group".group_filter AS gf
        SET update_user_id = v_update_user_id
          , update_date    = timezone('utc' :: TEXT, now())
        WHERE gf.group_id = v_group_id
        RETURNING gf.group_filter_id
      )
      INSERT INTO history_cte (group_filter_id)
        SELECT cte.group_filter_id
        FROM cte;

      DELETE FROM "group".group_filter AS gf
      USING history_cte
      WHERE gf.group_filter_id = history_cte.group_filter_id;
      DROP TABLE IF EXISTS history_cte;

      --profiles
      CREATE TEMP TABLE history_cte (
        group_profile_id INT
      );
      WITH cte AS (
        UPDATE "group".group_profile AS gp
        SET update_date    = timezone('utc' :: TEXT, now())
          , update_user_id = v_update_user_id
        WHERE gp.group_id = v_group_id
        RETURNING gp.group_profile_id
      )
      INSERT INTO history_cte (group_profile_id)
        SELECT cte.group_profile_id
        FROM cte;

      DELETE FROM "group".group_profile AS gp
      USING history_cte
      WHERE gp.group_profile_id = history_cte.group_profile_id;
      DROP TABLE IF EXISTS history_cte;

      --groups
      --remove from tree, both children and parents
      DELETE FROM "group".relationship_tree AS t
      WHERE t.path ~ CAST (
      '*.' ||       --remove all trees that contain the group (it is overlay, so they are all 'soft'
      v_group_id:: TEXT
      || '.*'
      AS lquery) IS TRUE;

      --remove relationships, both children and parents
      CREATE TEMP TABLE history_cte (
        group_relationship_id INT
      );
      WITH cte AS (
        UPDATE "group".group_relationship AS r
        SET update_date    = timezone('utc' :: TEXT, now())
          , update_user_id = v_update_user_id
        WHERE r.group_id = v_group_id
              OR r.related_group_id = v_group_id
        RETURNING r.group_relationship_id
      )
      INSERT INTO history_cte (group_relationship_id)
        SELECT cte.group_relationship_id
        FROM cte;

      DELETE FROM "group".group_relationship AS r
      USING history_cte
      WHERE r.group_relationship_id = history_cte.group_relationship_id;
      DROP TABLE IF EXISTS history_cte;

      UPDATE "group"."group" AS r
      SET update_date    = timezone('utc' :: TEXT, now())
        , update_user_id = v_update_user_id
      WHERE r.group_id = v_group_id;

      DELETE FROM "group"."group" AS g
      WHERE g.group_id = v_group_id;

      RETURN QUERY SELECT
                     TRUE,
                     '' :: TEXT;
    WHEN v_group_type_id = 2
    THEN --FOR ORG, REMOVE FIRST LEVEL ASSOCIATIONS, PARENT ONLY
      --remove all permissions from group
      CREATE TEMP TABLE history_cte (
        group_permission_id INT
      );
      WITH cte AS (
        UPDATE "group".group_permission AS gp
        SET update_user_id = v_update_user_id
          , update_date    = timezone('utc' :: TEXT, now())
        WHERE gp.group_id = v_group_id
        RETURNING gp.group_permission_id
      )
      INSERT INTO history_cte (group_permission_id)
        SELECT cte.group_permission_id
        FROM cte;

      DELETE FROM "group".group_permission AS gp
      USING history_cte
      WHERE gp.group_permission_id = history_cte.group_permission_id;
      DROP TABLE IF EXISTS history_cte;

      --Delete from relationship_tree
      --Since this is an org group, we need to delete from the relationship tree
      -- any tree that ends in the group_id
      DELETE  FROM  "group".relationship_tree AS t
      WHERE t.path ~ CAST(
                         '*.' || v_group_id :: TEXT
                         AS LQUERY) IS TRUE;

      --Delete from group_relationship where related_group_id = v_group_id
      CREATE TEMP TABLE history_cte (
        group_relationship_id INT
      );
      WITH cte AS (
        UPDATE "group".group_relationship AS r
        SET update_date    = timezone('utc' :: TEXT, now())
          , update_user_id = v_update_user_id
        WHERE r.related_group_id = v_group_id
        RETURNING r.group_relationship_id
      )
      INSERT INTO history_cte (group_relationship_id)
        SELECT cte.group_relationship_id
        FROM cte;

      DELETE FROM "group".group_relationship AS r
      USING history_cte
      WHERE r.group_relationship_id = history_cte.group_relationship_id;
      DROP TABLE IF EXISTS history_cte;

      --Delete from group
      UPDATE "group"."group" AS r
      SET update_date    = timezone('utc' :: TEXT, now())
        , update_user_id = v_update_user_id
      WHERE r.group_id = v_group_id;

      DELETE FROM "group"."group" AS g
      WHERE g.group_id = v_group_id;

      DROP TABLE IF EXISTS history_cte;

      RETURN QUERY SELECT
                     TRUE,
                     '' :: TEXT;
  END CASE;
  DROP TABLE IF EXISTS history_cte;
END;
$$;


--
-- Name: FUNCTION f_group_delete(v_group_id integer, v_update_user_id integer); Type: COMMENT; Schema: group; Owner: -
--

COMMENT ON FUNCTION f_group_delete(v_group_id integer, v_update_user_id integer) IS 'Deletes a group';


--
-- Name: f_group_delete_tree(integer, integer, integer); Type: FUNCTION; Schema: group; Owner: -
--

CREATE FUNCTION f_group_delete_tree(v_client_id integer, v_group_id integer, v_update_user_id integer) RETURNS TABLE(success boolean, message text)
    LANGUAGE plpgsql
    AS $$
DECLARE
  v_group_type_id "group"."group".group_type_id%TYPE;
BEGIN
    --validate that this is org group
  SELECT g.group_type_id
  INTO v_group_type_id
  FROM "group"."group" AS g
  WHERE g.group_id = v_group_id
    AND g.client_id = v_client_id;
  CASE
    WHEN NOT FOUND
    THEN
      RETURN QUERY SELECT
                     FALSE,
                     'group does not exists' :: TEXT;
      --if group is org and has descendants, exit with failure
    WHEN v_group_type_id != 2 THEN --org
      RETURN QUERY SELECT
                     FALSE,
                     'group is not an organization group' :: TEXT;
    ELSE
    --get list of groups to delete
    CREATE TEMP TABLE tmp_groups AS
      SELECT g.group_id,
        t.path,
        ltree2text( subpath(t.path,-1,1))::INT  AS related_group_id
       /* ,CASE WHEN nlevel(t.path) > 1 THEN
          ltree2text( subpath(t.path,-2,1))::INT
          ELSE NULL::INT
          END AS direct_parent_group_id
          */
    FROM "group"."group" AS g
      JOIN "group".relationship_tree t ON t.path ~ CAST (
        '*.' || v_group_id::TEXT || '.*'
        AS LQUERY)
    WHERE
     g.group_id = v_group_id;

    --remove all permissions from group
      CREATE TEMP TABLE history_cte (
        group_permission_id INT
      );
      WITH cte AS (
        UPDATE "group".group_permission AS gp
        SET update_user_id = v_update_user_id
          , update_date    = timezone('utc' :: TEXT, now())
        WHERE gp.group_id IN (SELECT tmp_groups.related_group_id FROM tmp_groups)
        RETURNING gp.group_permission_id
      )
      INSERT INTO history_cte (group_permission_id)
        SELECT cte.group_permission_id
        FROM cte;

      DELETE FROM "group".group_permission AS gp
      USING history_cte
      WHERE gp.group_permission_id = history_cte.group_permission_id;
      DROP TABLE IF EXISTS history_cte;

      -- remove all filters from group
      CREATE TEMP TABLE history_cte (
        group_filter_id INT
      );
      WITH cte AS (
        UPDATE "group".group_filter AS gf
        SET update_user_id = v_update_user_id
          , update_date    = timezone('utc' :: TEXT, now())
        WHERE gf.group_id IN (SELECT tmp_groups.related_group_id FROM tmp_groups)
        RETURNING gf.group_filter_id
      )
      INSERT INTO history_cte (group_filter_id)
        SELECT cte.group_filter_id
        FROM cte;

      DELETE FROM "group".group_filter AS gf
      USING history_cte
      WHERE gf.group_filter_id = history_cte.group_filter_id;
      DROP TABLE IF EXISTS history_cte;

      --profiles
      CREATE TEMP TABLE history_cte (
        group_profile_id INT
      );
      WITH cte AS (
        UPDATE "group".group_profile AS gp
        SET update_date    = timezone('utc' :: TEXT, now())
          , update_user_id = v_update_user_id
        WHERE gp.group_id IN (SELECT tmp_groups.related_group_id FROM tmp_groups)
        RETURNING gp.group_profile_id
      )
      INSERT INTO history_cte (group_profile_id)
        SELECT cte.group_profile_id
        FROM cte;

      DELETE FROM "group".group_profile AS gp
      USING history_cte
      WHERE gp.group_profile_id = history_cte.group_profile_id;
      DROP TABLE IF EXISTS history_cte;  --remove permissions

    --remove from tree
      DELETE FROM "group".relationship_tree AS t
      USING tmp_groups
      WHERE
        --REMOVE ALL PATHS THAT CONTAIN THE RELATED GROUPS (WILL REMOVE RELATIONSHIPS TO PARENT OVERLAY GROUPS AS WELL)
        t.path ~ CAST ( '*.' || tmp_groups.related_group_id::TEXT || '.*' AS lquery)
      ;

    --remove relationships
    CREATE TEMP TABLE history_cte (
        group_relationship_id INT
      );
      WITH cte AS (
        UPDATE "group".group_relationship AS r
        SET update_date    = timezone('utc' :: TEXT, now())
          , update_user_id = v_update_user_id
        FROM tmp_groups
        WHERE
          --REMOVE ALL RELATIONSHIPS THAT CONTAIN THE RELATED GROUPS, EITHER AS CHILD OR PARENT (WILL REMOVE RELATIONSHIPS TO PARENT OVERLAY GROUPS AS WELL)
              r.group_id = tmp_groups.related_group_id
              OR r.related_group_id = tmp_groups.related_group_id
        RETURNING r.group_relationship_id
      )
      INSERT INTO history_cte (group_relationship_id)
        SELECT cte.group_relationship_id
        FROM cte;

      DELETE FROM "group".group_relationship AS r
      USING history_cte
      WHERE r.group_relationship_id = history_cte.group_relationship_id;
      DROP TABLE IF EXISTS history_cte;

    --remove groups
      UPDATE "group"."group" AS r
      SET update_date    = timezone('utc' :: TEXT, now())
        , update_user_id = v_update_user_id
      WHERE r.group_id IN (SELECT tmp_groups.related_group_id FROM tmp_groups);

      DELETE FROM "group"."group" AS g
      WHERE g.group_id IN (SELECT tmp_groups.related_group_id FROM tmp_groups);

    --return success
    RETURN QUERY SELECT  TRUE, ''::TEXT;
    DROP TABLE IF EXISTS tmp_groups;
    END CASE ;
  END;
  $$;


--
-- Name: FUNCTION f_group_delete_tree(v_client_id integer, v_group_id integer, v_update_user_id integer); Type: COMMENT; Schema: group; Owner: -
--

COMMENT ON FUNCTION f_group_delete_tree(v_client_id integer, v_group_id integer, v_update_user_id integer) IS 'Deletes the group and all it''s child groups (recursively down the tree)';


--
-- Name: f_group_filter_delete(json, integer); Type: FUNCTION; Schema: group; Owner: -
--

CREATE FUNCTION f_group_filter_delete(v_filters json, v_update_user_id integer) RETURNS TABLE(group_id integer, channel_id bigint, group_name text, success boolean, message text)
    LANGUAGE plpgsql
    AS $$
  DECLARE
  BEGIN
    CREATE TEMP TABLE groups_to_create AS
      SELECT
      y."groupId" as group_id
      , z.value::BIGINT AS channel_id
      , null::SMALLINT as group_type_id
      , null::TEXT as group_name
      FROM
        (
        SELECT
        x."groupId"
        , x.channels
        FROM json_to_recordset(v_filters) as x("groupId" INT, "channels" JSON)
      ) AS y
      CROSS JOIN json_array_elements_text(y.channels) as z
      ;

    UPDATE groups_to_create AS g
      SET group_type_id = g2.group_type_id
    FROM "group"."group" AS g2
    WHERE g.group_id = g2.group_id;

    CREATE TEMP TABLE cte_output (group_id INT, channel_id INT);
    CREATE TEMP TABLE history_cte  (group_filter_id INT);

    WITH cte AS (
      UPDATE "group".group_filter AS gf
      SET update_date = timezone('utc'::text,now())
          , update_user_id = v_update_user_id
      FROM groups_to_create g
        WHERE g.channel_id = gf.channel_id AND g.group_id = gf.group_id
        AND g.group_type_id = 2 --only overlay groups can have filters
        RETURNING gf.group_filter_id
    )
    INSERT INTO history_cte (group_filter_id)
      SELECT cte.group_filter_id FROM cte;

    WITH cte AS (
    DELETE FROM "group".group_filter AS gf
    USING  history_cte g
      WHERE g.group_filter_id = gf.group_filter_id
      RETURNING gf.group_id, gf.channel_id
    )
    INSERT INTO cte_output (group_id, channel_id)
    SELECT cte.group_id, cte.channel_id FROM cte
    ;

      RETURN QUERY SELECT
        g.group_id
      , g.channel_id
      , g.group_name
      , CASE WHEN c.group_id IS NULL THEN FALSE
          ELSE TRUE END
      , CASE
          WHEN g.group_type_id != 2 THEN 'group is not an overlay group'::TEXT
          WHEN g.group_type_id IS NULL THEN 'group does not exist'::TEXT
          WHEN c.group_id IS NULL AND g.group_type_id = 2 THEN 'could not remove group filter'::TEXT
          ELSE ''::TEXT END
      FROM cte_output c
      RIGHT OUTER JOIN groups_to_create g
      ON g.group_id = c.group_id AND  g.channel_id = c.channel_id
      ;

    DROP TABLE IF EXISTS cte_output, groups_to_create, history_cte ;
  END;
  $$;


--
-- Name: FUNCTION f_group_filter_delete(v_filters json, v_update_user_id integer); Type: COMMENT; Schema: group; Owner: -
--

COMMENT ON FUNCTION f_group_filter_delete(v_filters json, v_update_user_id integer) IS 'Removes filters to one or more groups. Only overlay groups can have filters';


--
-- Name: f_group_filter_insert(json, integer); Type: FUNCTION; Schema: group; Owner: -
--

CREATE FUNCTION f_group_filter_insert(v_filters json, v_update_user_id integer) RETURNS TABLE(group_id integer, channel_id bigint, group_name text, success boolean, message text)
    LANGUAGE plpgsql
    AS $$
  DECLARE
  BEGIN
    CREATE TEMP TABLE groups_to_create AS
      SELECT
      y."groupId" as group_id
      , z.value::BIGINT AS channel_id
      , null::SMALLINT as group_type_id
      , null::TEXT as group_name
      FROM
        (
        SELECT
        x."groupId"
        , x.channels
        FROM json_to_recordset(v_filters) as x("groupId" INT, "channels" JSON)
      ) AS y
      CROSS JOIN json_array_elements_text(y.channels) as z
      ;

    UPDATE groups_to_create AS g
      SET group_type_id = g2.group_type_id
    FROM "group"."group" AS g2
    WHERE g.group_id = g2.group_id;

    CREATE TEMP TABLE cte_output (group_id INT, channel_id INT);
    WITH cte AS (
    INSERT INTO "group".group_filter (group_id, channel_id,update_user_id)
    SELECT g.group_id, g.channel_id,v_update_user_id
      FROM groups_to_create g
      WHERE g.group_type_id = 2 --only overlay groups can have filters
      ON CONFLICT DO NOTHING
      RETURNING group_filter.group_id, group_filter.channel_id
    )
    INSERT INTO cte_output (group_id, channel_id)
    SELECT cte.group_id, cte.channel_id FROM cte
    ;

      RETURN QUERY SELECT
        g.group_id
      , g.channel_id
      , g.group_name
      , CASE WHEN c.group_id IS NULL THEN FALSE
          ELSE TRUE END
      , CASE
          WHEN g.group_type_id != 2 THEN 'group is not an overlay group'::TEXT
          WHEN g.group_type_id IS NULL THEN 'group does not exist'::TEXT
          WHEN c.group_id IS NULL AND g.group_type_id = 2 THEN 'could not add group filter'::TEXT
          ELSE ''::TEXT END
      FROM cte_output c
      RIGHT OUTER JOIN groups_to_create g
      ON g.group_id = c.group_id AND  g.channel_id = c.channel_id
      ;

    DROP TABLE IF EXISTS cte_output, groups_to_create;
  END;
  $$;


--
-- Name: FUNCTION f_group_filter_insert(v_filters json, v_update_user_id integer); Type: COMMENT; Schema: group; Owner: -
--

COMMENT ON FUNCTION f_group_filter_insert(v_filters json, v_update_user_id integer) IS 'Assigns filters to one or more groups. Only overlay groups can have filters';


--
-- Name: f_group_insert(text, smallint, integer, text, integer, json, integer); Type: FUNCTION; Schema: group; Owner: -
--

CREATE FUNCTION f_group_insert(v_name text, v_group_type_id smallint, v_client_id integer, v_external_id text, v_source_id integer, v_channel_ids json, v_update_user_id integer) RETURNS TABLE(group_id integer, success boolean, message text)
    LANGUAGE plpgsql
    AS $$
  DECLARE
    v_group_id "group".group.group_id%TYPE;
  BEGIN
    --Validations
    CASE
      --If type is organization, verify the client has a root group
      WHEN v_group_type_id = 2 AND NOT EXISTS (
          SELECT 1 FROM "group"."group" AS g WHERE g.client_id = v_client_id AND g.group_type_id = 1
            )
        THEN RETURN QUERY SELECT null::INT, FALSE, 'client has no root group'::TEXT;
      --if type is 'Root' and group with type 'Root' exists for the client
      WHEN v_group_type_id = 1 AND EXISTS (
          SELECT 1 FROM "group"."group" AS g WHERE g.client_id = v_client_id AND g.group_type_id = 1
            )
        THEN RETURN QUERY SELECT null::INT, FALSE, 'root group exists for client'::TEXT;
      WHEN COALESCE(v_external_id,''::TEXT) !='' AND EXISTS (
          SELECT 1 FROM "group"."group" AS g WHERE g.client_id = v_client_id AND external_id = v_external_id
        )
        THEN RETURN QUERY SELECT null::INT, FALSE, 'duplicate external id'::TEXT;
      ELSE
        --create group
        INSERT INTO "group"."group"  AS g (group_type_id, name, client_id, update_user_id,  parent_group_id, source_id, external_id)
        VALUES (v_group_type_id,v_name,v_client_id,v_update_user_id,NULL, v_source_id,v_external_id)
        RETURNING g.group_id INTO v_group_id;

        --create group filters
        IF v_channel_ids IS NOT NULL THEN
          INSERT INTO "group".group_filter (channel_id, group_id, update_user_id)
          SELECT x.channel_id::INT
            , v_group_id
            , v_update_user_id
          FROM json_array_elements_text(v_channel_ids) as x(channel_id);
        END IF;

      --since this group has no parent, insert it into relationship_tree
        INSERT INTO "group".relationship_tree (group_relationship_id, path)
        VALUES (NULL::INT,text2ltree(v_group_id::TEXT));
        RETURN QUERY SELECT v_group_id, TRUE, ''::TEXT;
      END CASE ;
  END;
  $$;


--
-- Name: FUNCTION f_group_insert(v_name text, v_group_type_id smallint, v_client_id integer, v_external_id text, v_source_id integer, v_channel_ids json, v_update_user_id integer); Type: COMMENT; Schema: group; Owner: -
--

COMMENT ON FUNCTION f_group_insert(v_name text, v_group_type_id smallint, v_client_id integer, v_external_id text, v_source_id integer, v_channel_ids json, v_update_user_id integer) IS 'Adds a group with optional filters';


--
-- Name: f_group_permission_delete(bigint, json, integer); Type: FUNCTION; Schema: group; Owner: -
--

CREATE FUNCTION f_group_permission_delete(v_user_id bigint, v_groups json, v_update_user_id integer) RETURNS TABLE(group_id integer, user_id bigint, success boolean, message text)
    LANGUAGE plpgsql
    AS $$
  DECLARE
  BEGIN

    CREATE TEMP TABLE perm AS
    SELECT g.group_id::INT,
      CASE WHEN g2.group_id IS NULL THEN FALSE ELSE TRUE END as group_exists
    FROM json_array_elements_text(v_groups) g(group_id)
    LEFT OUTER JOIN "group"."group" g2 ON g2.group_id = g.group_id::INT;

    CREATE TEMP TABLE cte_output (group_id INT);
    CREATE TEMP TABLE history_cte (group_permission_id INT);
    WITH cte AS (
        UPDATE "group".group_permission gp
          SET update_date = timezone('utc'::text,now())
          , update_user_id = v_update_user_id
        FROM perm as g
        WHERE g.group_exists IS TRUE
          AND g.group_id = gp.group_id
          AND v_user_id = gp.user_id
        RETURNING gp.group_permission_id
     )
    INSERT INTO history_cte(group_permission_id)
    SELECT cte.group_permission_id FROM cte;

    WITH cte AS (
      DELETE FROM "group".group_permission gp
      USING history_cte as g
      WHERE g.group_permission_id = gp.group_permission_id
      RETURNING gp.group_id
    )
    INSERT INTO cte_output (group_id)
    SELECT cte.group_id FROM cte
    ;

      RETURN QUERY SELECT
        g.group_id
      , v_user_id
      , CASE WHEN c.group_id IS NULL THEN FALSE
          ELSE TRUE END
      , CASE
          WHEN g.group_exists IS FALSE THEN 'group does not exist'::TEXT
          WHEN c.group_id IS NULL THEN 'could not add remove permission'::TEXT
          ELSE ''::TEXT END
      FROM cte_output c
      RIGHT OUTER JOIN perm AS g
      ON g.group_id = c.group_id
      ;

    DROP TABLE IF EXISTS cte_output,perm,history_cte;
  END;
  $$;


--
-- Name: FUNCTION f_group_permission_delete(v_user_id bigint, v_groups json, v_update_user_id integer); Type: COMMENT; Schema: group; Owner: -
--

COMMENT ON FUNCTION f_group_permission_delete(v_user_id bigint, v_groups json, v_update_user_id integer) IS 'Removes permission on groups to a user';


--
-- Name: f_group_permission_insert(bigint, json, integer); Type: FUNCTION; Schema: group; Owner: -
--

CREATE FUNCTION f_group_permission_insert(v_user_id bigint, v_groups json, v_update_user_id integer) RETURNS TABLE(group_id integer, user_id bigint, success boolean, message text)
    LANGUAGE plpgsql
    AS $$
  DECLARE
  BEGIN

    CREATE TEMP TABLE perm AS
    SELECT g.group_id::INT,
      CASE WHEN g2.group_id IS NULL THEN FALSE ELSE TRUE END as group_exists
    FROM json_array_elements_text(v_groups) g(group_id)
    LEFT OUTER JOIN "group"."group" g2 ON g2.group_id = g.group_id::INT;

    CREATE TEMP TABLE cte_output (group_id INT);
    WITH cte AS (
      INSERT INTO "group".group_permission (group_id, user_id,update_user_id)
      SELECT g.group_id, v_user_id,v_update_user_id
      FROM perm AS g
        WHERE g.group_exists IS TRUE
      ON CONFLICT DO NOTHING
      RETURNING group_permission.group_id
    )
    INSERT INTO cte_output (group_id)
    SELECT cte.group_id FROM cte
    ;

      RETURN QUERY SELECT
        g.group_id
      , v_user_id
      , CASE WHEN c.group_id IS NULL THEN FALSE
          ELSE TRUE END
      , CASE
          WHEN g.group_exists IS FALSE THEN 'group does not exist'::TEXT
          WHEN c.group_id IS NULL THEN 'could not add group permission'::TEXT
          ELSE ''::TEXT END
      FROM cte_output c
      RIGHT OUTER JOIN perm AS g
      ON g.group_id = c.group_id
      ;

    DROP TABLE IF EXISTS cte_output,perm;
  END;
  $$;


--
-- Name: FUNCTION f_group_permission_insert(v_user_id bigint, v_groups json, v_update_user_id integer); Type: COMMENT; Schema: group; Owner: -
--

COMMENT ON FUNCTION f_group_permission_insert(v_user_id bigint, v_groups json, v_update_user_id integer) IS 'Assigns permission on groups to a user';


--
-- Name: f_group_permission_select(bigint); Type: FUNCTION; Schema: group; Owner: -
--

CREATE FUNCTION f_group_permission_select(v_user_id bigint) RETURNS TABLE(group_id integer)
    LANGUAGE plpgsql IMMUTABLE
    AS $$
  DECLARE

  BEGIN

      RETURN QUERY SELECT
        gp.group_id
      FROM "group".group_permission gp
      WHERE gp.user_id = v_user_id
    ;
  END;
  $$;


--
-- Name: FUNCTION f_group_permission_select(v_user_id bigint); Type: COMMENT; Schema: group; Owner: -
--

COMMENT ON FUNCTION f_group_permission_select(v_user_id bigint) IS 'Gets tbe permissions assigned to the user';


--
-- Name: f_group_profile_delete(integer, integer, json, integer); Type: FUNCTION; Schema: group; Owner: -
--

CREATE FUNCTION f_group_profile_delete(v_client_id integer, v_group_id integer, v_profiles json, v_update_user_id integer) RETURNS TABLE(profile_id bigint, group_id integer, success boolean, message text)
    LANGUAGE plpgsql
    AS $$
DECLARE
  v_group_type_id SMALLINT;
BEGIN
  SELECT g.group_type_id
  INTO v_group_type_id
  FROM "group"."group" AS g
  WHERE g.group_id = v_group_id
  AND g.client_id = v_client_id
  ;
  IF NOT FOUND
  THEN RETURN QUERY SELECT
                      NULL :: BIGINT,
                      v_group_id,
                      FALSE,
                      'Group does not exist for client' :: TEXT;
  ELSE

    CREATE TEMP TABLE history_cte (group_profile_id INT);
    CREATE TEMP TABLE cte_output (profile_id BIGINT);
    WITH cte AS (
        UPDATE "group".group_profile AS gp
          SET update_date = timezone('utc'::text,now())
          , update_user_id = v_update_user_id
          WHERE v_group_id = gp.group_id
          AND gp.profile_id IN (SELECT x.profileId::BIGINT FROM json_array_elements_text(v_profiles) as x(profileId))
        RETURNING gp.group_profile_id
      )
    INSERT INTO history_cte (group_profile_id)
      SELECT cte.group_profile_id
      FROM cte;

    WITH cte AS (
      DELETE FROM "group".group_profile AS gp
        USING history_cte
      WHERE gp.group_profile_id = history_cte.group_profile_id
      RETURNING gp.profile_id
      )
    INSERT INTO cte_output (profile_id)
      SELECT cte.profile_id FROM cte
    ;

      RETURN QUERY SELECT
        x."profileId"::BIGINT
      , v_group_id
      , CASE WHEN c.profile_id IS NOT NULL THEN TRUE
            ELSE FALSE END
      , CASE WHEN c.profile_id IS NOT NULL THEN ''::TEXT
          ELSE 'group/profile relationship did not exist'::TEXT END
      FROM json_array_elements_text(v_profiles) as x("profileId")
      LEFT OUTER JOIN cte_output c ON c.profile_id = x."profileId"::BIGINT
      ;
    DROP TABLE IF EXISTS cte_output, history_cte;
    END IF ;
  END;
  $$;


--
-- Name: FUNCTION f_group_profile_delete(v_client_id integer, v_group_id integer, v_profiles json, v_update_user_id integer); Type: COMMENT; Schema: group; Owner: -
--

COMMENT ON FUNCTION f_group_profile_delete(v_client_id integer, v_group_id integer, v_profiles json, v_update_user_id integer) IS 'Unassigns profiles from a group';


--
-- Name: f_group_profile_insert(integer, integer, json, integer); Type: FUNCTION; Schema: group; Owner: -
--

CREATE FUNCTION f_group_profile_insert(v_client_id integer, v_group_id integer, v_profiles json, v_update_user_id integer) RETURNS TABLE(profile_id bigint, group_id integer, success boolean, message text)
    LANGUAGE plpgsql
    AS $$
DECLARE
  v_group_type_id SMALLINT;
BEGIN
  SELECT g.group_type_id
  INTO v_group_type_id
  FROM "group"."group" AS g
  WHERE g.group_id = v_group_id
  AND g.client_id = v_client_id
  ;
  IF NOT FOUND
  THEN RETURN QUERY SELECT
                      NULL :: BIGINT,
                      v_group_id,
                      FALSE,
                      'Group does not exist' :: TEXT;
  ELSE

    CREATE TEMP TABLE tmp_profiles AS
      SELECT x.profile_id :: BIGINT
      ,NULL::INT AS  client_id
      FROM json_array_elements_text(v_profiles) AS x("profile_id");

    UPDATE tmp_profiles
      SET client_id =   p.client_id
      FROM profile.profile p
      WHERE tmp_profiles.profile_id = p.profile_id;

    IF v_group_type_id  IN (1,2)
    THEN
      CREATE TEMP TABLE cte_output (
        group_profile_id INT
      );
      --if the group is an org group, remove any references of these profiles to other org groups
      WITH cte AS (
        UPDATE "group".group_profile AS gp
        SET update_user_id = v_update_user_id
          , update_date    = timezone('utc' :: TEXT, now())
        FROM tmp_profiles AS p, "group"."group" g
        WHERE p.profile_id = gp.profile_id
              AND p.client_id = v_client_id
              AND g.group_id = gp.group_id
              AND g.group_type_id IN (1, 2)
              AND g.client_id = v_client_id
              AND g.group_id != v_group_id
        RETURNING gp.group_profile_id
      )
      INSERT INTO cte_output (group_profile_id)
        SELECT cte.group_profile_id
        FROM cte;

      DELETE FROM "group".group_profile AS gp
      USING cte_output
      WHERE cte_output.group_profile_id = gp.group_profile_id;
      DROP TABLE IF EXISTS cte_output;
    END IF;

    CREATE TEMP TABLE cte_output (
      profile_id BIGINT
    );
    WITH cte AS (
      INSERT INTO "group".group_profile (group_id, profile_id, update_user_id)
        SELECT
          v_group_id,
          x.profile_id,
          v_update_user_id
        FROM tmp_profiles AS x
        WHERE x.client_id = v_client_id
      ON CONFLICT DO NOTHING
      RETURNING group_profile.profile_id
    )
    INSERT INTO cte_output (profile_id)
      SELECT cte.profile_id
      FROM cte;

    RETURN QUERY SELECT
                   x.profile_id,
                   v_group_id,
                   CASE WHEN c.profile_id IS NULL
                     THEN FALSE
                   ELSE TRUE END,
                   CASE
                   WHEN x.client_id != v_client_id
                     THEN 'profile does not exist for client' :: TEXT
                   WHEN c.profile_id IS NULL
                     THEN 'could not add profile' :: TEXT
                   ELSE '' :: TEXT END
                 FROM tmp_profiles AS x
                   LEFT OUTER JOIN cte_output c ON x.profile_id :: BIGINT = c.profile_id;

    DROP TABLE IF EXISTS cte_output,tmp_profiles;
  END IF;
END;
$$;


--
-- Name: FUNCTION f_group_profile_insert(v_client_id integer, v_group_id integer, v_profiles json, v_update_user_id integer); Type: COMMENT; Schema: group; Owner: -
--

COMMENT ON FUNCTION f_group_profile_insert(v_client_id integer, v_group_id integer, v_profiles json, v_update_user_id integer) IS 'Assigns profiles to a group';


--
-- Name: f_group_profile_select(integer, integer); Type: FUNCTION; Schema: group; Owner: -
--

CREATE FUNCTION f_group_profile_select(v_client_id integer, v_group_id integer) RETURNS TABLE(profile_id bigint, parent_group_ids json, first_name text, last_name text, organization_name text, is_internal boolean, time_zone_abbrev character varying, external_facing_id character varying, profile_type_id smallint)
    LANGUAGE plpgsql
    AS $$
  DECLARE
  BEGIN

    --get list of groups that are downstream
    CREATE TEMP TABLE tmp_groups AS
      SELECT
        DISTINCT
        ltree2text( subpath(t.path,-1,1))::INT  AS group_id
       /* ,CASE WHEN nlevel(t.path) > 1 THEN
          ltree2text( subpath(t.path,-2,1))::INT
          ELSE NULL::INT
          END AS direct_parent_group_id
          */
    FROM "group"."group" AS g
      JOIN "group".relationship_tree t ON t.path ~ CAST (
        '*.' || v_group_id::TEXT || '.*'
        AS LQUERY)
    WHERE
     g.group_id = v_group_id
      AND g.client_id = v_client_id;

    --Get list of profiles from those groups
    --Aggregate the parent groups into JSON, group by all other columns
    RETURN QUERY
    WITH parents AS (
      SELECT
        gp.profile_id
      ,  json_agg(g.group_id) as parent_group_ids
      FROM tmp_groups g
      JOIN "group".group_profile gp
        ON gp.group_id = g.group_id
      GROUP BY   gp.profile_id
      )
      SELECT
        p.profile_id
      , parents.parent_group_ids
      , p.first_name
      , p.last_name
      , p.organization_name
      , p.is_internal
      , p.time_zone_abbrev
      , p.external_facing_id
      , p.profile_type_id
      FROM parents
      JOIN profile.profile p
        ON p.profile_id = parents.profile_id
      ;
    DROP TABLE IF EXISTS tmp_groups;
  END;
  $$;


--
-- Name: FUNCTION f_group_profile_select(v_client_id integer, v_group_id integer); Type: COMMENT; Schema: group; Owner: -
--

COMMENT ON FUNCTION f_group_profile_select(v_client_id integer, v_group_id integer) IS 'Gets tbe profiles that are assigned to the group and any child groups';


--
-- Name: f_group_relationship_delete(integer, integer, integer, json); Type: FUNCTION; Schema: group; Owner: -
--

CREATE FUNCTION f_group_relationship_delete(v_client_id integer, v_group_id integer, v_update_user_id integer, v_groups json) RETURNS TABLE(group_id integer, child_group_id integer, success boolean, message text)
    LANGUAGE plpgsql
    AS $$
BEGIN
  PERFORM set_config('IdentityAPI.update_user_id',v_update_user_id::TEXT,TRUE);
  CASE WHEN NOT EXISTS(SELECT group_type_id
              FROM "group"."group" g
              WHERE g.group_id = v_group_id
                AND g.group_type_id = 3 --overlay
                AND g.client_id = v_client_id
        )
    THEN RETURN QUERY SELECT v_group_id, NULL::INT, FALSE,'specified group is not an overlay group'::TEXT;
  WHEN EXISTS(SELECT group_type_id
              FROM "group"."group" g
              JOIN json_array_elements_text(v_groups) AS x(value)
                ON x.value::INT = g.group_id
              WHERE g.group_type_id = 3 --overlay
        )
    THEN RETURN QUERY SELECT v_group_id, NULL::INT, FALSE,'at least one of the groups to relate is not an overlay group'::TEXT;
  ELSE
    --delete set of paths that have *.group_id.related_group_id.*
    DELETE FROM "group".relationship_tree AS t
    WHERE t.path ?   array(
      SELECT
      CAST( '*.' || v_group_id::TEXT ||'.' || x.value || '.*' AS lquery)
      FROM json_array_elements_text(v_groups) AS x(value)
    )IS TRUE;

    --delete from group_relationship table
    CREATE TEMP TABLE history_cte  (group_relationship_id INT, related_group_id INT);
    WITH cte AS (
      UPDATE "group".group_relationship AS r
        SET update_date = timezone('utc'::text,now())
          , update_user_id = v_update_user_id
        WHERE r.group_id = v_group_id AND r.related_group_id IN (
          SELECT x.value::INT FROM  json_array_elements_text(v_groups) AS x(value)
          )
      RETURNING r.group_relationship_id, r.related_group_id
      )
      INSERT INTO history_cte(group_relationship_id, related_group_id)
      SELECT cte.group_relationship_id , cte.related_group_id
      FROM cte;

    DELETE FROM "group".group_relationship AS r
      USING history_cte
    WHERE r.group_relationship_id = history_cte.group_relationship_id
    ;

    --return status
    RETURN QUERY SELECT v_group_id,
      x.value::INT,
      CASE WHEN c.related_group_id IS NULL THEN FALSE
                 ELSE  TRUE END,
      CASE WHEN c.related_group_id IS NULL THEN 'group was not a valid child'
        ELSE ''::TEXT END
    FROM json_array_elements_text(v_groups) AS x(value)
    LEFT OUTER JOIN history_cte AS c ON x.value::INT = c.related_group_id
    ;
    DROP TABLE IF EXISTS history_cte;
  END CASE;
END;
$$;


--
-- Name: f_group_relationship_insert(integer, integer, integer, json); Type: FUNCTION; Schema: group; Owner: -
--

CREATE FUNCTION f_group_relationship_insert(v_client_id integer, v_group_id integer, v_update_user_id integer, v_groups json) RETURNS TABLE(group_id integer, child_group_id integer, success boolean, message text)
    LANGUAGE plpgsql
    AS $$
DECLARE
  v_group_type_id SMALLINT;
  v_lquery_ancestors lquery;
BEGIN
  PERFORM set_config('IdentityAPI.update_user_id',v_update_user_id::TEXT,TRUE);
  --GET GROUP TYPE
SELECT group_type_id into v_group_type_id
              FROM "group"."group" g
              WHERE g.group_id = v_group_id
              AND g.client_id = v_client_id
  ;
  --PREPARE LQUERY FOR TREE OPERATIONS
  v_lquery_ancestors:=CAST('*.' || v_group_id::TEXT AS lquery);
CREATE TABLE tmp_groups AS
  --EXPAND JSON
  SELECT x.value::INT as related_group_id,
    group_type_id,
    name
              FROM "group"."group" g
              RIGHT OUTER JOIN json_array_elements_text(v_groups) AS x(value)
                ON x.value::INT = g.group_id
  ;
CASE
  WHEN EXISTS(SELECT 1 FROM tmp_groups WHERE v_group_id = tmp_groups.related_group_id) THEN
    RETURN QUERY SELECT v_group_id,v_group_id,FALSE ,'Parent can not be the same as child'::TEXT;
  WHEN EXISTS(SELECT 1 FROM tmp_groups WHERE tmp_groups.group_type_id = 1) THEN
    RETURN QUERY SELECT v_group_id,null::int,FALSE ,'Root can not be a child'::TEXT;
  WHEN EXISTS(
    SELECT 1 FROM (
      SELECT tmp_groups.name
      FROM tmp_groups
      UNION ALL
      SELECT child_group.name
      FROM "group"."group" AS child_group
        JOIN "group".group_relationship gr
          ON gr.related_group_id = child_group.group_id
          AND gr.group_id = v_group_id
      ) as s
    GROUP BY s.name
    HAVING COUNT(*) > 1
  ) THEN
    RETURN QUERY SELECT v_group_id,null::int,FALSE ,'Duplicate names in child groups'::TEXT;
  WHEN v_group_type_id IN  (1,2) AND exists(SELECT 1 FROM tmp_groups WHERE tmp_groups.group_type_id=3) THEN
    RETURN QUERY SELECT v_group_id,null::int,FALSE ,'Overlay can not be child of root or organization'::TEXT;
  WHEN v_group_type_id = 2 AND NOT  exists(SELECT 1 FROM "group".group_relationship gr  WHERE gr.related_group_id = v_group_id AND gr.relationship_type_id = 1) THEN
    RETURN QUERY SELECT v_group_id,null::int,FALSE ,'Parent must be part of existing tree to add children'::TEXT;
  --end validations

  WHEN v_group_type_id IN  (1,2)  THEN
       IF EXISTS(
        SELECT 1 FROM "group".group_relationship gr
          JOIN tmp_groups ON tmp_groups.related_group_id = gr.related_group_id -- we already know that the child groups are Org from validations above
        ) THEN --this case is a move- we will need to rehome the tree
              --Do this by:
              --delete the existing parents for any child trees (which will force all children to have no parent of type Org or Root)
         CREATE TEMP TABLE cte_output (group_relationship_id INT, group_id INT, related_group_id INT, lquery_child_paths lquery);
         WITH cte AS (
           UPDATE "group".group_relationship AS gr
             SET update_date= timezone('utc'::TEXT,now())
            , update_user_id = v_update_user_id
           FROM tmp_groups
           WHERE tmp_groups.related_group_id = gr.related_group_id
            AND gr.relationship_type_id = 1 --only remove parents that are org/root
           RETURNING gr.group_relationship_id, gr.group_id,gr.related_group_id
           )
         INSERT INTO cte_output(group_relationship_id , group_id,related_group_id ,lquery_child_paths )
           SELECT cte.group_relationship_id, cte.group_id, cte.related_group_id
           -- find all paths *.C.* for child C
             , CAST (
                 '*.' ||
                 cte.related_group_id::TEXT ||
                 '.*'
               AS lquery)
           FROM cte;
         DELETE FROM "group".group_relationship AS gr
          USING cte_output
         WHERE cte_output.group_relationship_id = gr.group_relationship_id;

         --update paths that have *.C.* to C.*
         WITH  child_cte AS (
          SELECT subltree(child_paths.path, index(child_paths.path, text2ltree( c.related_group_id::TEXT)),nlevel(child_paths.path)) as newpath,
            child_paths.path
          FROM cte_output AS c
          JOIN "group".relationship_tree AS child_paths
            ON child_paths.path ~ c.lquery_child_paths
          )
        UPDATE "group".relationship_tree AS t
          SET path = child_cte.newpath
        FROM child_cte
        WHERE t.path = child_cte.path
          ;
        DROP TABLE IF EXISTS cte_output;
        END IF;
    CREATE TEMP TABLE cte_output (group_id INT, related_group_id INT, group_relationship_id INT, lquery_child_paths lquery);
    WITH cte AS (
    INSERT INTO "group".group_relationship (group_id, relationship_type_id, related_group_id, update_user_id)
    SELECT v_group_id
      ,1::SMALLINT --LINK
      ,x.value::INT
      ,v_update_user_id
    FROM json_array_elements_text(v_groups) AS x(value)
      ON CONFLICT DO NOTHING
      RETURNING group_relationship.group_id,group_relationship.related_group_id,group_relationship.group_relationship_id
    )
      INSERT INTO cte_output (group_id, related_group_id, group_relationship_id,lquery_child_paths)
      SELECT cte.group_id,cte.related_group_id,cte.group_relationship_id
        ,CAST (cte.related_group_id::TEXT ||         -- we will update these path with a new root = the ancestor
              '.*'
              AS lquery) --filter  for second insert,
      FROM cte
    ;
    /* For Org (and Root type) groups, we update the existing Child paths, since they will always start
  For any parent P and children C
  CROSS JOIN all paths *.P, and C.* (Org groups will never have a parent that is another org group, since we deleted that relationship already)
   Join all *.P with all C.* and Update C.* --> *.P.C.*
   */
    WITH anc_cte AS (
      SELECT anscestors.path
      FROM "group".relationship_tree AS anscestors
      WHERE anscestors.path ~ v_lquery_ancestors
      )
    , child_cte AS (
      SELECT child_paths.path,
        c.group_relationship_id
      FROM cte_output AS c
      JOIN "group".relationship_tree AS child_paths
        ON child_paths.path ~ c.lquery_child_paths
      )
    UPDATE "group".relationship_tree AS t
      SET path = anc_cte.path || child_cte.path
        , group_relationship_id = child_cte.group_relationship_id
    FROM child_cte, anc_cte
    WHERE t.path = child_cte.path
      ;

    RETURN QUERY SELECT v_group_id,
      tmp_groups.related_group_id,
      CASE WHEN c.related_group_id IS NOT NULL THEN TRUE
        ELSE FALSE END ,
      CASE WHEN c.related_group_id IS NOT NULL THEN ''::TEXT
        ELSE 'did not add relationship, unknown error'::TEXT END
    FROM cte_output c
    RIGHT OUTER JOIN tmp_groups
        ON tmp_groups.related_group_id = c.related_group_id
    ;
  WHEN v_group_type_id = 3 THEN -- OVERLAY
    --insert into group_relationship
    CREATE TEMP TABLE cte_output (group_id INT, related_group_id INT, group_relationship_id INT, lquery_child_paths lquery);
    WITH cte AS (
    INSERT INTO "group".group_relationship (group_id, relationship_type_id, related_group_id, update_user_id)
    SELECT v_group_id
      ,2::SMALLINT --LINK
      ,x.value::INT
      ,v_update_user_id
    FROM json_array_elements_text(v_groups) AS x(value)
      ON CONFLICT DO NOTHING
      RETURNING group_relationship.group_id,group_relationship.related_group_id,group_relationship.group_relationship_id
    )
      INSERT INTO cte_output (group_id, related_group_id, group_relationship_id, lquery_child_paths)
      SELECT cte.group_id,cte.related_group_id,cte.group_relationship_id
        ,CAST('*.' ||                               --filter to find  any paths that contain the child
              cte.related_group_id::TEXT ||         -- we will add a version of these path with a new root = the ancestor
              '.*'
              AS lquery) --filter  for second insert,
      FROM cte
    ;
  /* For Overlay groups, we only create new links
  For any parent P and children C
  CROSS JOIN all paths *.P, and *.C.*
   Join all *.P with all C.* and insert *.P.C.*
   */
    WITH anc_cte AS (
      SELECT DISTINCT anscestors.path
      FROM "group".relationship_tree AS anscestors
      WHERE anscestors.path ~ v_lquery_ancestors
      )
    , child_cte AS (
      SELECT DISTINCT c.group_relationship_id,
        subltree(child_paths.path, index(child_paths.path, text2ltree( c.related_group_id::TEXT)),nlevel(child_paths.path)) as path
      FROM cte_output AS c
      JOIN "group".relationship_tree AS child_paths
        ON child_paths.path ~ c.lquery_child_paths
      )
    INSERT INTO "group".relationship_tree (group_relationship_id, path)
      SELECT child_cte.group_relationship_id,
        anc_cte.path || child_cte.path
      FROM child_cte, anc_cte
      ;

    RETURN QUERY SELECT v_group_id,
      tmp_groups.related_group_id,
      CASE WHEN c.related_group_id IS NOT NULL THEN TRUE
        ELSE FALSE END ,
      CASE WHEN c.related_group_id IS NOT NULL THEN ''::TEXT
        ELSE 'did not add relationship, unknown error'::TEXT END
    FROM cte_output c
    RIGHT OUTER JOIN tmp_groups
        ON tmp_groups.related_group_id = c.related_group_id
    ;
  END CASE ;
  DROP TABLE IF EXISTS cte_output,tmp_groups;
END;
$$;


--
-- Name: f_group_relationship_select_linked_groups(integer); Type: FUNCTION; Schema: group; Owner: -
--

CREATE FUNCTION f_group_relationship_select_linked_groups(v_group_id integer) RETURNS TABLE(group_id integer)
    LANGUAGE plpgsql IMMUTABLE
    AS $$
  DECLARE

  BEGIN

      RETURN QUERY SELECT
        g.related_group_id
      FROM "group".group_relationship g
      WHERE g.group_id = v_group_id
        AND relationship_type_id = 2
    ;
  END;
  $$;


--
-- Name: FUNCTION f_group_relationship_select_linked_groups(v_group_id integer); Type: COMMENT; Schema: group; Owner: -
--

COMMENT ON FUNCTION f_group_relationship_select_linked_groups(v_group_id integer) IS 'Gets tbe org groups that are assigned to the overlay group';


--
-- Name: f_group_select(integer, integer); Type: FUNCTION; Schema: group; Owner: -
--

CREATE FUNCTION f_group_select(v_client_id integer, v_group_id integer) RETURNS TABLE(group_id integer, group_name text, group_type_id smallint, source_id integer, external_id text, child_group_ids json, profile_ids json, parent_group_ids json, channel_ids json)
    LANGUAGE plpgsql
    AS $$
  DECLARE

  BEGIN
    CREATE TEMP TABLE tmp_groups AS
      SELECT g.group_id
      , g.name as group_name
      , g.group_type_id
      , g.source_id
      , g.external_id
      , NULL::JSON AS child_group_ids
      , NULL::JSON AS profile_ids
      , NULL::JSON AS parent_group_ids
      , NULL::JSON AS channel_ids
    FROM "group"."group" AS g
    WHERE g.group_id = v_group_id
    AND g.client_id = v_client_id;
    --get profiles JSON
    WITH cte AS (
      SELECT json_agg(p.profile_id) as profile_ids
        , p.group_id
      FROM "group".group_profile p
        JOIN tmp_groups AS t
        ON p.group_id = t.group_id
      GROUP BY p.group_id
      )
    UPDATE tmp_groups
      SET profile_ids = cte.profile_ids
    FROM cte
    WHERE tmp_groups.group_id = cte.group_id;
    --get filters JSON
    WITH cte AS (
      SELECT json_agg(p.channel_id) as channel_ids
        , p.group_id
      FROM "group".group_filter p
        JOIN tmp_groups AS t
        ON p.group_id = t.group_id
      GROUP BY p.group_id
      )
    UPDATE tmp_groups
      SET channel_ids = cte.channel_ids
    FROM cte
    WHERE tmp_groups.group_id = cte.group_id;

    --get child groups JSON
    WITH cte AS (
      SELECT json_agg(p.related_group_id) as child_group_ids
        , p.group_id
      FROM "group".group_relationship p
        JOIN tmp_groups AS t
        ON p.group_id = t.group_id
      GROUP BY p.group_id
      )
    UPDATE tmp_groups
      SET child_group_ids = cte.child_group_ids
    FROM cte
    WHERE tmp_groups.group_id = cte.group_id;

    --get parent groups JSON
    WITH cte AS (
      SELECT json_agg(p.group_id) as parent_group_ids
        , p.related_group_id as group_id
      FROM "group".group_relationship p
        JOIN tmp_groups AS t
        ON p.related_group_id = t.group_id
      GROUP BY p.related_group_id
      )
    UPDATE tmp_groups
      SET parent_group_ids = cte.parent_group_ids
    FROM cte
    WHERE tmp_groups.group_id = cte.group_id;

    RETURN QUERY SELECT
     t.group_id
    ,t.group_name
    ,t.group_type_id
    ,t.source_id
    ,t.external_id
    ,t.child_group_ids
    ,t.profile_ids
    ,t.parent_group_ids
    ,t.channel_ids
    FROM tmp_groups AS t;

    DROP TABLE IF EXISTS tmp_groups;
  END;
  $$;


--
-- Name: FUNCTION f_group_select(v_client_id integer, v_group_id integer); Type: COMMENT; Schema: group; Owner: -
--

COMMENT ON FUNCTION f_group_select(v_client_id integer, v_group_id integer) IS 'Gets the group with all it''s attributes ';


--
-- Name: f_group_select_all(integer, smallint, boolean); Type: FUNCTION; Schema: group; Owner: -
--

CREATE FUNCTION f_group_select_all(v_client_id integer, v_group_type_id smallint, v_show_only_orphans boolean) RETURNS TABLE(group_id integer, group_name text, group_type_id smallint, source_id integer, external_id text, child_group_ids json, profile_ids json, parent_group_ids json, channel_ids json)
    LANGUAGE plpgsql
    AS $$
  BEGIN
    v_show_only_orphans = COALESCE(v_show_only_orphans,FALSE);

    CREATE TEMP TABLE tmp_groups AS
      SELECT g.group_id
      , g.name as group_name
      , g.group_type_id
      , g.source_id
      , g.external_id
      , NULL::JSON AS child_group_ids
      , NULL::JSON AS profile_ids
      , NULL::JSON AS parent_group_ids
      , NULL::JSON AS channel_ids
    FROM "group"."group" AS g
    WHERE
     g.client_id = v_client_id
    AND (v_group_type_id IS NULL OR v_group_type_id = g.group_type_id)
    AND (v_show_only_orphans IS FALSE OR NOT EXISTS (         -- IF FLAG IS FALSE (OR NULL), ONLY SHOW GROUPS THAT ARE NOT A CHILD OF ANYTHING
         SELECT 1 FROM "group".group_relationship gr WHERE gr.related_group_id = g.group_id
          )
       )

    ;

    --get profiles JSON
    WITH cte AS (
      SELECT json_agg(p.profile_id) as profile_ids
        , p.group_id
      FROM "group".group_profile p
        JOIN tmp_groups AS t
        ON p.group_id = t.group_id
      GROUP BY p.group_id
      )
    UPDATE tmp_groups
      SET profile_ids = cte.profile_ids
    FROM cte
    WHERE tmp_groups.group_id = cte.group_id;
    --get filters JSON
    WITH cte AS (
      SELECT json_agg(p.channel_id) as channel_ids
        , p.group_id
      FROM "group".group_filter p
        JOIN tmp_groups AS t
        ON p.group_id = t.group_id
      GROUP BY p.group_id
      )
    UPDATE tmp_groups
      SET channel_ids = cte.channel_ids
    FROM cte
    WHERE tmp_groups.group_id = cte.group_id;

    --get child groups JSON
    WITH cte AS (
      SELECT json_agg(p.related_group_id) as child_group_ids
        , p.group_id
      FROM "group".group_relationship p
        JOIN tmp_groups AS t
        ON p.group_id = t.group_id
      GROUP BY p.group_id
      )
    UPDATE tmp_groups
      SET child_group_ids = cte.child_group_ids
    FROM cte
    WHERE tmp_groups.group_id = cte.group_id;

    --get parent groups JSON
    WITH cte AS (
      SELECT json_agg(p.group_id) as parent_group_ids
        , p.related_group_id as group_id
      FROM "group".group_relationship p
        JOIN tmp_groups AS t
        ON p.related_group_id = t.group_id
      GROUP BY p.related_group_id
      )
    UPDATE tmp_groups
      SET parent_group_ids = cte.parent_group_ids
    FROM cte
    WHERE tmp_groups.group_id = cte.group_id;

    RETURN QUERY SELECT
     t.group_id
    ,t.group_name
    ,t.group_type_id
    ,t.source_id
    ,t.external_id
    ,t.child_group_ids
    ,t.profile_ids
    ,t.parent_group_ids
    ,t.channel_ids
    FROM tmp_groups AS t;

    DROP TABLE IF EXISTS tmp_groups;
  END;
  $$;


--
-- Name: FUNCTION f_group_select_all(v_client_id integer, v_group_type_id smallint, v_show_only_orphans boolean); Type: COMMENT; Schema: group; Owner: -
--

COMMENT ON FUNCTION f_group_select_all(v_client_id integer, v_group_type_id smallint, v_show_only_orphans boolean) IS 'Gets all groups for a client with filters';


--
-- Name: f_group_select_all(integer, smallint, boolean, integer, integer); Type: FUNCTION; Schema: group; Owner: -
--

CREATE FUNCTION f_group_select_all(v_client_id integer, v_group_type_id smallint, v_show_only_orphans boolean, v_limit integer, v_offset integer) RETURNS TABLE(group_id integer, group_name text, group_type_id smallint, source_id integer, external_id text, child_group_ids json, profile_ids json, parent_group_ids json, channel_ids json)
    LANGUAGE plpgsql
    AS $$
  BEGIN
    v_show_only_orphans = COALESCE(v_show_only_orphans,FALSE);

    CREATE TEMP TABLE tmp_groups AS
      SELECT g.group_id
      , g.name as group_name
      , g.group_type_id
      , g.source_id
      , g.external_id
      , NULL::JSON AS child_group_ids
      , NULL::JSON AS profile_ids
      , NULL::JSON AS parent_group_ids
      , NULL::JSON AS channel_ids
    FROM "group"."group" AS g
    WHERE
     g.client_id = v_client_id
    AND (v_group_type_id IS NULL OR v_group_type_id = g.group_type_id)
    AND (v_show_only_orphans IS FALSE OR NOT EXISTS (         -- IF FLAG IS FALSE (OR NULL), ONLY SHOW GROUPS THAT ARE NOT A CHILD OF ANYTHING
         SELECT 1 FROM "group".group_relationship gr WHERE gr.related_group_id = g.group_id
          )
       )
    ORDER BY g.name ASC OFFSET v_offset LIMIT v_limit
    ;

    --get profiles JSON
    WITH cte AS (
      SELECT json_agg(p.profile_id) as profile_ids
        , p.group_id
      FROM "group".group_profile p
        JOIN tmp_groups AS t
        ON p.group_id = t.group_id
      GROUP BY p.group_id
      )
    UPDATE tmp_groups
      SET profile_ids = cte.profile_ids
    FROM cte
    WHERE tmp_groups.group_id = cte.group_id;
    --get filters JSON
    WITH cte AS (
      SELECT json_agg(p.channel_id) as channel_ids
        , p.group_id
      FROM "group".group_filter p
        JOIN tmp_groups AS t
        ON p.group_id = t.group_id
      GROUP BY p.group_id
      )
    UPDATE tmp_groups
      SET channel_ids = cte.channel_ids
    FROM cte
    WHERE tmp_groups.group_id = cte.group_id;

    --get child groups JSON
    WITH cte AS (
      SELECT json_agg(p.related_group_id) as child_group_ids
        , p.group_id
      FROM "group".group_relationship p
        JOIN tmp_groups AS t
        ON p.group_id = t.group_id
      GROUP BY p.group_id
      )
    UPDATE tmp_groups
      SET child_group_ids = cte.child_group_ids
    FROM cte
    WHERE tmp_groups.group_id = cte.group_id;

    --get parent groups JSON
    WITH cte AS (
      SELECT json_agg(p.group_id) as parent_group_ids
        , p.related_group_id as group_id
      FROM "group".group_relationship p
        JOIN tmp_groups AS t
        ON p.related_group_id = t.group_id
      GROUP BY p.related_group_id
      )
    UPDATE tmp_groups
      SET parent_group_ids = cte.parent_group_ids
    FROM cte
    WHERE tmp_groups.group_id = cte.group_id;

    RETURN QUERY SELECT
     t.group_id
    ,t.group_name
    ,t.group_type_id
    ,t.source_id
    ,t.external_id
    ,t.child_group_ids
    ,t.profile_ids
    ,t.parent_group_ids
    ,t.channel_ids
    FROM tmp_groups AS t;

    DROP TABLE IF EXISTS tmp_groups;
  END;
  $$;


--
-- Name: FUNCTION f_group_select_all(v_client_id integer, v_group_type_id smallint, v_show_only_orphans boolean, v_limit integer, v_offset integer); Type: COMMENT; Schema: group; Owner: -
--

COMMENT ON FUNCTION f_group_select_all(v_client_id integer, v_group_type_id smallint, v_show_only_orphans boolean, v_limit integer, v_offset integer) IS 'Gets all groups for a client with filters';


--
-- Name: f_group_select_children(integer, integer); Type: FUNCTION; Schema: group; Owner: -
--

CREATE FUNCTION f_group_select_children(v_client_id integer, v_group_id integer) RETURNS TABLE(group_id integer, group_name text, group_type_id smallint, source_id integer, external_id text, child_group_ids json, profile_ids json, parent_group_ids json, channel_ids json)
    LANGUAGE plpgsql
    AS $$
  DECLARE

  BEGIN
    CREATE TEMP TABLE tmp_groups AS
      SELECT g.group_id
      , g.name as group_name
      , g.group_type_id
      , g.source_id
      , g.external_id
      , NULL::JSON AS child_group_ids
      , NULL::JSON AS profile_ids
      , NULL::JSON AS parent_group_ids
      , NULL::JSON AS channel_ids
    FROM "group"."group" AS g
      JOIN "group".group_relationship gr ON g.group_id = gr.related_group_id
    WHERE g.client_id = v_client_id
      AND gr.group_id = v_group_id;
    --get profiles JSON
    WITH cte AS (
      SELECT json_agg(p.profile_id) as profile_ids
        , p.group_id
      FROM "group".group_profile p
        JOIN tmp_groups AS t
        ON p.group_id = t.group_id
      GROUP BY p.group_id
      )
    UPDATE tmp_groups
      SET profile_ids = cte.profile_ids
    FROM cte
    WHERE tmp_groups.group_id = cte.group_id;
    --get filters JSON
    WITH cte AS (
      SELECT json_agg(p.channel_id) as channel_ids
        , p.group_id
      FROM "group".group_filter p
        JOIN tmp_groups AS t
        ON p.group_id = t.group_id
      GROUP BY p.group_id
      )
    UPDATE tmp_groups
      SET channel_ids = cte.channel_ids
    FROM cte
    WHERE tmp_groups.group_id = cte.group_id;

    --get child groups JSON
    WITH cte AS (
      SELECT json_agg(p.related_group_id) as child_group_ids
        , p.group_id
      FROM "group".group_relationship p
        JOIN tmp_groups AS t
        ON p.group_id = t.group_id
      GROUP BY p.group_id
      )
    UPDATE tmp_groups
      SET child_group_ids = cte.child_group_ids
    FROM cte
    WHERE tmp_groups.group_id = cte.group_id;

    --get parent groups JSON
    WITH cte AS (
      SELECT json_agg(p.group_id) as parent_group_ids
        , p.related_group_id as group_id
      FROM "group".group_relationship p
        JOIN tmp_groups AS t
        ON p.related_group_id = t.group_id
      GROUP BY p.related_group_id
      )
    UPDATE tmp_groups
      SET parent_group_ids = cte.parent_group_ids
    FROM cte
    WHERE tmp_groups.group_id = cte.group_id;

    RETURN QUERY SELECT
     t.group_id
    ,t.group_name
    ,t.group_type_id
    ,t.source_id
    ,t.external_id
    ,t.child_group_ids
    ,t.profile_ids
    ,t.parent_group_ids
    ,t.channel_ids
    FROM tmp_groups AS t;

    DROP TABLE IF EXISTS tmp_groups;
  END;
  $$;


--
-- Name: FUNCTION f_group_select_children(v_client_id integer, v_group_id integer); Type: COMMENT; Schema: group; Owner: -
--

COMMENT ON FUNCTION f_group_select_children(v_client_id integer, v_group_id integer) IS 'Gets the direct children of the group ';


--
-- Name: f_group_select_tree(integer, integer); Type: FUNCTION; Schema: group; Owner: -
--

CREATE FUNCTION f_group_select_tree(v_client_id integer, v_group_id integer) RETURNS TABLE(group_id integer, group_name text, group_type_id smallint, source_id integer, external_id text, child_group_ids json, profiles json, parent_group_ids json, channel_ids json, depth json)
    LANGUAGE plpgsql
    AS $$
BEGIN
  --get list of groups to return
  CREATE TEMP TABLE tmp_groups AS
    --get list of groups and their depths (one group can have more than one depth in the tree)
    --depths should start at zero, so only use the part of the path that starts with v_group_id
    WITH cte_tree as (
      SELECT ltree2text( subpath(t.path,-1,1))::INT  AS group_id -- the leaf level group, ids that are part of the tree twice will appear twice
            , nlevel( subpath( t.path, index(t.path,text2ltree(v_group_id::TEXT)))) - 1 AS   depth --the path depth, starting from v_group_id (-1 because we want it to start at zero
      FROM "group".relationship_tree t
      WHERE  t.path ~ CAST (
        '*.' || v_group_id::TEXT || '.*'                        --gets all paths that pass through v_group_id
        AS LQUERY)
      )
    -- aggregate the depths, so we have a distinct list of groups to return
    , cte_groups AS (
        SELECT t.group_id,
          json_agg (t.depth) as depth
        FROM cte_tree t
        GROUP BY t.group_id
      )
      SELECT g.group_id
        , g.name as group_name
        , g.group_type_id
        , g.source_id
        , g.external_id
        , NULL::JSON AS child_group_ids
        , NULL::JSON AS profiles
        , NULL::JSON AS parent_group_ids
        , NULL::JSON AS channel_ids
        ,  t.depth as depth
      FROM "group"."group" AS g
        JOIN cte_groups AS t ON t.group_id = g.group_id
      WHERE
       g.client_id = v_client_id
      ;

  --get profiles JSON
    WITH custom as (
      SELECT
        json_agg( CASE WHEN pav.value IS NOT NULL
                  THEN json_build_object('profileAttributeId', pa.profile_attribute_id,'name',pa.name,'value',pav.value)
                  ELSE NULL::JSON END )
        AS profileAttribute
      , p.profile_id,p.client_id,p.first_name,p.last_name,p.user_id,p.external_facing_id,p.update_user_id,p.update_date
      , p.profile_type_id,p.organization_name,p.is_internal,p.time_zone_abbrev
      , gp.group_id
      FROM profile.profile p
      JOIN  "group".group_profile gp
        ON p.profile_id = gp.profile_id
      JOIN tmp_groups AS t
          ON gp.group_id = t.group_id
      LEFT JOIN profile.profile_attribute_value pav
        ON pav.profile_id = p.profile_id
      LEFT JOIN profile.profile_attribute pa
          ON pa.profile_attribute_id = pav.profile_attribute_id
      GROUP BY p.profile_id,p.client_id,p.first_name,p.last_name,p.user_id,p.external_facing_id,p.update_user_id,p.update_date
        , p.profile_type_id,p.organization_name,p.is_internal,p.time_zone_abbrev
        , gp.group_id
      )
      ,cte AS (
      SELECT json_agg(json_build_object(
            'profileId', custom.profile_id
            ,'clientId', custom.client_id
            ,'firstName', custom.first_name
            ,'lastName', custom.last_name
            ,'userId', custom.user_id
            ,'externalFacingId',custom.external_facing_id
            ,'updateDate',custom.update_date
            ,'updateUserId', custom.update_user_id
            ,'organizationName',custom.organization_name
            ,'profileTypeId', custom.profile_type_id
            ,'internal',custom.is_internal
            ,'timeZoneId',custom.time_zone_abbrev
            ,'attributes',custom.profileAttribute
                      )) as profiles
        , custom.group_id
      FROM custom
      GROUP BY custom.group_id
      )
    UPDATE tmp_groups
      SET profiles = cte.profiles
    FROM cte
    WHERE tmp_groups.group_id = cte.group_id;
    --get filters JSON
    WITH cte AS (
      SELECT json_agg(p.channel_id) as channel_ids
        , p.group_id
      FROM "group".group_filter p
        JOIN tmp_groups AS t
        ON p.group_id = t.group_id
      GROUP BY p.group_id
      )
    UPDATE tmp_groups
      SET channel_ids = cte.channel_ids
    FROM cte
    WHERE tmp_groups.group_id = cte.group_id;

    --get child groups JSON
    WITH cte AS (
      SELECT json_agg(p.related_group_id) as child_group_ids
        , p.group_id
      FROM "group".group_relationship p
        JOIN tmp_groups AS t
        ON p.group_id = t.group_id
      GROUP BY p.group_id
      )
    UPDATE tmp_groups
      SET child_group_ids = cte.child_group_ids
    FROM cte
    WHERE tmp_groups.group_id = cte.group_id;

    --get parent groups JSON
    WITH cte AS (
      SELECT json_agg(p.group_id) as parent_group_ids
        , p.related_group_id as group_id
      FROM "group".group_relationship p
        JOIN tmp_groups AS t
        ON p.related_group_id = t.group_id
      WHERE p.group_id IN (SELECT t2.group_id FROM tmp_groups AS t2) --only return parent groups that are a part of this tree
      GROUP BY p.related_group_id
      )
    UPDATE tmp_groups
      SET parent_group_ids = cte.parent_group_ids
    FROM cte
    WHERE tmp_groups.group_id = cte.group_id;

  RETURN QUERY SELECT
     t.group_id
    ,t.group_name
    ,t.group_type_id
    ,t.source_id
    ,t.external_id
    ,t.child_group_ids
    ,t.profiles
    ,t.parent_group_ids
    ,t.channel_ids
    ,t.depth
    FROM tmp_groups AS t;

  DROP TABLE IF EXISTS tmp_groups;
  END;
  $$;


--
-- Name: FUNCTION f_group_select_tree(v_client_id integer, v_group_id integer); Type: COMMENT; Schema: group; Owner: -
--

COMMENT ON FUNCTION f_group_select_tree(v_client_id integer, v_group_id integer) IS 'Gets the group and all it''s child groups, profiles, and filters (recursively down the tree)';


--
-- Name: f_group_update(integer, text, json, integer); Type: FUNCTION; Schema: group; Owner: -
--

CREATE FUNCTION f_group_update(v_group_id integer, v_name text, v_channel_ids json, v_update_user_id integer) RETURNS TABLE(success boolean, message text)
    LANGUAGE plpgsql
    AS $$
  DECLARE
    v_group_type_id "group"."group".group_type_id%TYPE;
  BEGIN
    SELECT g.group_type_id INTO v_group_type_id
      FROM "group"."group" AS g WHERE g.group_id = v_group_id;
    CASE
      WHEN NOT FOUND THEN
        RETURN QUERY SELECT FALSE, 'group does not exists'::TEXT;
      WHEN v_group_type_id = 1 THEN --root
        RETURN QUERY SELECT FALSE, 'cannot update root'::TEXT;
      ELSE
        --get list of channels that are already present
        CREATE TEMP TABLE channels AS
        SELECT x.channel_id::INT
          ,gf2.group_filter_id
        FROM json_array_elements_text(v_channel_ids) AS x(channel_id)
        LEFT OUTER JOIN "group".group_filter AS gf2
          ON gf2.channel_id = x.channel_id::INT
          AND gf2.group_id = v_group_id;

        --update group record
        UPDATE "group"."group" AS g
          SET  name = v_name
          , update_user_id = v_update_user_id
          , update_date = timezone('utc'::text, now())
        WHERE v_group_id = g.group_id;
        --remove filters not present in update
        CREATE TEMP TABLE history_cte (group_filter_id INT);
        WITH cte AS (
          UPDATE "group".group_filter AS gf
            SET update_user_id = v_update_user_id
          , update_date = timezone('utc'::text, now())
          WHERE gf.group_id = v_group_id
          AND gf.channel_id NOT IN (
            SELECT x.channel_id
            FROM channels AS x
            )
          RETURNING gf.group_filter_id
        )
          INSERT INTO history_cte(group_filter_id)
          SELECT cte.group_filter_id FROM cte;

        DELETE FROM "group".group_filter AS gf
          USING history_cte
        WHERE gf.group_filter_id = history_cte.group_filter_id;
        --add in any additional filters
        INSERT INTO "group".group_filter(channel_id, group_id, update_user_id)
          SELECT x.channel_id, v_group_id,v_update_user_id
          FROM channels as x
          WHERE x.group_filter_id IS NULL
          ;
        RETURN  QUERY SELECT TRUE, ''::TEXT;
      END CASE;
    DROP TABLE IF EXISTS history_cte, channels;
  END;
  $$;


--
-- Name: FUNCTION f_group_update(v_group_id integer, v_name text, v_channel_ids json, v_update_user_id integer); Type: COMMENT; Schema: group; Owner: -
--

COMMENT ON FUNCTION f_group_update(v_group_id integer, v_name text, v_channel_ids json, v_update_user_id integer) IS 'Updates a group';


--
-- Name: trf_group_filter_insert_history(); Type: FUNCTION; Schema: group; Owner: -
--

CREATE FUNCTION trf_group_filter_insert_history() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
    BEGIN
        IF (TG_OP = 'DELETE') THEN
            INSERT INTO "group".group_filter_history(operation, group_filter_id, channel_id, group_id, update_user_id, update_date)
            SELECT  LOWER(substring(TG_OP,1,1)), OLD.group_filter_id,OLD.channel_id, OLD.group_id,OLD.update_user_id, OLD.update_date;
            RETURN OLD;
        ELSIF (TG_OP = ('UPDATE') OR TG_OP = ('INSERT')) THEN
           INSERT INTO "group".group_filter_history(operation, group_filter_id, channel_id, group_id, update_user_id, update_date)
           SELECT LOWER(substring(TG_OP,1,1)), NEW.group_filter_id, NEW.channel_id, NEW.group_id, NEW.update_user_id, NEW.update_date;
            RETURN NEW;
        END IF;
        RETURN NULL; -- result is ignored since this is an AFTER trigger
    END;
$$;


--
-- Name: trf_group_insert_history(); Type: FUNCTION; Schema: group; Owner: -
--

CREATE FUNCTION trf_group_insert_history() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
    BEGIN
        IF (TG_OP = 'DELETE') THEN
            INSERT INTO "group".group_history(group_id,client_id,external_id,group_type_id
              ,name, parent_group_id, source_id,operation, update_user_id, update_date)
            SELECT OLD.group_id,OLD.client_id,OLD.external_id,OLD.group_type_id
              ,OLD.name, OLD.parent_group_id, OLD.source_id, LOWER(substring(TG_OP,1,1)), OLD.update_user_id, OLD.update_date;
            RETURN OLD;
        ELSIF (TG_OP = ('UPDATE') OR TG_OP = ('INSERT')) THEN
           INSERT INTO "group".group_history(group_id,client_id,external_id,group_type_id
              ,name, parent_group_id, source_id,operation, update_user_id, update_date)
           SELECT NEW.group_id,NEW.client_id,NEW.external_id,NEW.group_type_id
              ,NEW.name, NEW.parent_group_id, NEW.source_id,LOWER(substring(TG_OP,1,1)), NEW.update_user_id, NEW.update_date;
            RETURN NEW;
        END IF;
        RETURN NULL; -- result is ignored since this is an AFTER trigger
    END;
$$;


--
-- Name: trf_group_permission_insert_history(); Type: FUNCTION; Schema: group; Owner: -
--

CREATE FUNCTION trf_group_permission_insert_history() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
    BEGIN
        IF (TG_OP = 'DELETE') THEN
            INSERT INTO "group".group_permission_history(operation,group_permission_id,group_id,user_id,update_user_id,update_date)
            SELECT LOWER(substring(TG_OP,1,1)), OLD.group_permission_id, OLD.group_id,OLD.user_id,OLD.update_user_id,OLD.update_date;
            RETURN OLD;
        ELSIF (TG_OP = ('UPDATE') OR TG_OP = ('INSERT')) THEN
           INSERT INTO "group".group_permission_history(operation,group_permission_id,group_id,user_id,update_user_id,update_date)
           SELECT LOWER(substring(TG_OP,1,1)), NEW.group_permission_id, NEW.group_id, NEW.user_id, NEW.update_user_id, NEW.update_date;
            RETURN NEW;
        END IF;
        RETURN NULL; -- result is ignored since this is an AFTER trigger
    END;
$$;


--
-- Name: trf_group_profile_insert_history(); Type: FUNCTION; Schema: group; Owner: -
--

CREATE FUNCTION trf_group_profile_insert_history() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
    BEGIN
        IF (TG_OP = 'DELETE') THEN
            INSERT INTO "group".group_profile_history(operation,group_profile_id,group_id, profile_id,update_user_id,update_date   )
           SELECT LOWER(substring(TG_OP,1,1)), OLD.group_profile_id, OLD.group_id, OLD.profile_id, OLD.update_user_id, OLD.update_date;
            RETURN OLD;
        ELSIF (TG_OP = ('UPDATE') OR TG_OP = ('INSERT')) THEN
           INSERT INTO "group".group_profile_history(operation,group_profile_id,group_id, profile_id,update_user_id,update_date   )
           SELECT LOWER(substring(TG_OP,1,1)), NEW.group_profile_id, NEW.group_id, NEW.profile_id, NEW.update_user_id, NEW.update_date;
            RETURN NEW;
        END IF;
        RETURN NULL; -- result is ignored since this is an AFTER trigger
    END;
$$;


--
-- Name: trf_group_relationship_insert_history(); Type: FUNCTION; Schema: group; Owner: -
--

CREATE FUNCTION trf_group_relationship_insert_history() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
    DECLARE
      v_update_user_id INT;
    BEGIN
        SELECT current_setting('IdentityAPI.update_user_id') INTO v_update_user_id;
        IF (TG_OP = 'DELETE') THEN
            INSERT INTO "group".group_relationship_history(operation,group_relationship_id,group_id, related_group_id, relationship_type_id,update_user_id,update_date)
            SELECT LOWER(substring(TG_OP,1,1)), OLD.group_relationship_id, OLD.group_id, OLD.related_group_id,OLD.relationship_type_id, v_update_user_id, OLD.update_date;
            RETURN OLD;
        ELSIF (TG_OP = ('UPDATE') OR TG_OP = ('INSERT')) THEN
           INSERT INTO "group".group_relationship_history(operation,group_relationship_id,group_id, related_group_id, relationship_type_id,update_user_id,update_date)
           SELECT LOWER(substring(TG_OP,1,1)), NEW.group_relationship_id, NEW.group_id, NEW.related_group_id,NEW.relationship_type_id, v_update_user_id, NEW.update_date;
            RETURN NEW;
        END IF;
        RETURN NULL; -- result is ignored since this is an AFTER trigger
    END;
$$;


SET search_path = profile, pg_catalog;

--
-- Name: f_channel_identifier_profile_delete(bigint); Type: FUNCTION; Schema: profile; Owner: -
--

CREATE FUNCTION f_channel_identifier_profile_delete(v_channel_identifier_profile_id bigint) RETURNS TABLE(succeeded boolean, message text)
    LANGUAGE plpgsql
    AS $$
  BEGIN
        DELETE FROM profile.channel_identifier_profile as ci
        WHERE
          ci.channel_identifier_profile_id =  v_channel_identifier_profile_id
        ;
        IF NOT FOUND THEN RETURN QUERY
          SELECT FALSE, 'association did not exist'::TEXT;
        ELSE RETURN QUERY
          SELECT TRUE,''::TEXT;
        END IF ;
  END;
  $$;


--
-- Name: FUNCTION f_channel_identifier_profile_delete(v_channel_identifier_profile_id bigint); Type: COMMENT; Schema: profile; Owner: -
--

COMMENT ON FUNCTION f_channel_identifier_profile_delete(v_channel_identifier_profile_id bigint) IS 'Removes entirely the association between profile and channel identifier';


--
-- Name: f_channel_identifier_profile_insert(bigint, bigint, timestamp without time zone, timestamp without time zone, boolean, integer, text); Type: FUNCTION; Schema: profile; Owner: -
--

CREATE FUNCTION f_channel_identifier_profile_insert(v_profile_id bigint, v_channel_identifier_id bigint, v_effective_from timestamp without time zone, v_effective_to timestamp without time zone, v_is_primary boolean, v_update_user_id integer, v_display_name text) RETURNS TABLE(channel_identifier_profile_id bigint, succeeded boolean, message text)
    LANGUAGE plpgsql
    AS $$
  DECLARE v_client_id INT;
      v_channel_identifier_profile_id  profile.channel_identifier_profile.channel_identifier_profile_id%TYPE;
  BEGIN
    --Get client and channel identifier ids
    SELECT p.client_id
    INTO v_client_id
    FROM profile.profile AS  p
    WHERE p.profile_id =v_profile_id;
    IF NOT FOUND THEN
		RETURN QUERY SELECT
                   NULL::BIGINT,
                   FALSE,
                   'profile does not exist' :: TEXT;
    ELSIF EXISTS( --verify that the new dates don't have any overlap with any profile owned by the same client
        SELECT 1 FROM profile.channel_identifier_profile as cip
        WHERE cip.client_id = v_client_id
        AND cip.channel_identifier_id = v_channel_identifier_id
        AND
          (TSRANGE(effective_from,effective_to) &&
            TSRANGE(v_effective_from
            ,v_effective_to)) = TRUE::BOOL --date ranges overlap
          )
      THEN RETURN QUERY SELECT
                            NULL::BIGINT
                          , FALSE::BOOLEAN
                          , 'effective dates overlap'::TEXT;
    ELSE

    INSERT INTO profile.channel_identifier_profile(effective_from, effective_to, update_user_id, channel_identifier_id, profile_id, is_primary, display_name, client_id)
        VALUES (
          v_effective_from
          ,v_effective_to
          ,v_update_user_id
          ,v_channel_identifier_id
          ,v_profile_id
          ,v_is_primary
          ,v_display_name
          ,v_client_id
        )
        RETURNING channel_identifier_profile.channel_identifier_profile_id INTO v_channel_identifier_profile_id
        ;
        RETURN QUERY SELECT v_channel_identifier_profile_id, TRUE ::BOOLEAN,''::TEXT;

    END IF;
  END;
  $$;


--
-- Name: FUNCTION f_channel_identifier_profile_insert(v_profile_id bigint, v_channel_identifier_id bigint, v_effective_from timestamp without time zone, v_effective_to timestamp without time zone, v_is_primary boolean, v_update_user_id integer, v_display_name text); Type: COMMENT; Schema: profile; Owner: -
--

COMMENT ON FUNCTION f_channel_identifier_profile_insert(v_profile_id bigint, v_channel_identifier_id bigint, v_effective_from timestamp without time zone, v_effective_to timestamp without time zone, v_is_primary boolean, v_update_user_id integer, v_display_name text) IS 'Creates an association between profile and channel identifer';


--
-- Name: f_channel_identifier_profile_move(bigint, bigint, bigint, timestamp without time zone, bigint, timestamp without time zone, integer); Type: FUNCTION; Schema: profile; Owner: -
--

CREATE FUNCTION f_channel_identifier_profile_move(v_channel_identifier_profile_id bigint, v_channel_identifier_id bigint, v_source_profile_id bigint, v_effective_to timestamp without time zone, v_destination_profile_id bigint, v_effective_from timestamp without time zone, v_update_user_id integer) RETURNS TABLE(succeeded boolean, message text)
    LANGUAGE plpgsql
    AS $$
DECLARE time_now                TIMESTAMP;
        v_client_id             INT;
BEGIN
  time_now := timezone('utc' :: TEXT, now());
  --Effective start date should Defaults to v_effective_to
  v_effective_from = COALESCE(v_effective_from,v_effective_to);
  --Get client and channel identifier profile ids for the association we want to update
  SELECT
    cip.client_id
  INTO v_client_id
  FROM profile.channel_identifier_profile AS cip
  WHERE
    cip.channel_identifier_id = v_channel_identifier_id
    AND cip.profile_id = v_source_profile_id
    AND cip.channel_identifier_profile_id = v_channel_identifier_profile_id
  ;
  IF v_client_id IS NULL
  THEN
    RETURN QUERY SELECT
                   FALSE,
                   'source profile association does not exist' :: TEXT;
  ELSIF NOT EXISTS (SELECT 1 FROM profile.profile p WHERE p.profile_id = v_destination_profile_id AND p.client_id = v_client_id)
  THEN 
		RETURN QUERY SELECT
                   FALSE,
                   'destination profile does not exist for client' :: TEXT;
  ELSE    
	  --end association
      UPDATE profile.channel_identifier_profile
      SET effective_to = v_effective_to
      WHERE channel_identifier_profile_id = v_channel_identifier_profile_id;

      INSERT INTO profile.channel_identifier_profile (effective_from, effective_to, update_user_id, update_date, channel_identifier_id, profile_id, is_primary,client_id)
      VALUES (
        v_effective_from
        , NULL :: TIMESTAMP
        , v_update_user_id
        , time_now
        , v_channel_identifier_id
        , v_destination_profile_id
        , FALSE
        , v_client_id
      );
      RETURN QUERY SELECT
                     TRUE :: BOOLEAN,
                     '' :: TEXT;
    END IF;
END;
$$;


--
-- Name: FUNCTION f_channel_identifier_profile_move(v_channel_identifier_profile_id bigint, v_channel_identifier_id bigint, v_source_profile_id bigint, v_effective_to timestamp without time zone, v_destination_profile_id bigint, v_effective_from timestamp without time zone, v_update_user_id integer); Type: COMMENT; Schema: profile; Owner: -
--

COMMENT ON FUNCTION f_channel_identifier_profile_move(v_channel_identifier_profile_id bigint, v_channel_identifier_id bigint, v_source_profile_id bigint, v_effective_to timestamp without time zone, v_destination_profile_id bigint, v_effective_from timestamp without time zone, v_update_user_id integer) IS 'Moves the association for a Channel Identifier from one profile to another';


--
-- Name: f_channel_identifier_profile_select(bigint); Type: FUNCTION; Schema: profile; Owner: -
--

CREATE FUNCTION f_channel_identifier_profile_select(v_profile_id bigint) RETURNS TABLE(channel_identifier_profile_id bigint, channel_identifier_id bigint, effective_from timestamp without time zone, effective_to timestamp without time zone, is_primary boolean, display_name text)
    LANGUAGE plpgsql IMMUTABLE
    AS $$
  BEGIN
        RETURN QUERY SELECT
         cip.channel_identifier_profile_id
        ,cip.channel_identifier_id
        ,cip.effective_from
        ,cip.effective_to
        ,cip.is_primary
        ,cip.display_name
        FROM profile.channel_identifier_profile cip
        WHERE cip.profile_id = v_profile_id
    ;
  END;
  $$;


--
-- Name: FUNCTION f_channel_identifier_profile_select(v_profile_id bigint); Type: COMMENT; Schema: profile; Owner: -
--

COMMENT ON FUNCTION f_channel_identifier_profile_select(v_profile_id bigint) IS 'Get the list of Channel Identifiers associated with a profile';


--
-- Name: f_channel_identifier_profile_update(bigint, timestamp without time zone, timestamp without time zone, integer, boolean, text); Type: FUNCTION; Schema: profile; Owner: -
--

CREATE FUNCTION f_channel_identifier_profile_update(v_channel_identifier_profile_id bigint, v_effective_from timestamp without time zone, v_effective_to timestamp without time zone, v_update_user_id integer, v_is_primary boolean, v_display_name text) RETURNS TABLE(succeeded boolean, message text)
    LANGUAGE plpgsql
    AS $$

  DECLARE v_client_id INT;
     v_channel_identifier_id BIGINT;
  BEGIN
    --Get client and channel identifier ids for the association we want to update
    SELECT p.client_id, cip.channel_identifier_id
    INTO v_client_id, v_channel_identifier_id
    FROM profile.channel_identifier_profile as cip
      JOIN profile.profile AS  p ON cip.profile_id = p.profile_id
    WHERE
      cip.channel_identifier_profile_id =  v_channel_identifier_profile_id
    ;

    --verify that the new dates don't have any overlap with any profile owned by the same client/channel_id association
    IF EXISTS(
        SELECT 1 FROM profile.channel_identifier_profile cip
          JOIN profile.profile as p on cip.profile_id = p.profile_id
        WHERE p.client_id = v_client_id
        AND cip.channel_identifier_id = v_channel_identifier_id
        AND cip.channel_identifier_profile_id
            !=  v_channel_identifier_profile_id
        AND
          (TSRANGE(effective_from,effective_to) &&
            TSRANGE(v_effective_from
            ,v_effective_to)) = TRUE::BOOL --date ranges overlap
          )
      THEN RETURN QUERY SELECT FALSE::BOOLEAN, 'effective dates overlap'::TEXT;
      ELSE

        UPDATE profile.channel_identifier_profile SET
          effective_from = v_effective_from
          , effective_to = v_effective_to
          , update_user_id =v_update_user_id
          , update_date = timezone('utc'::text, now())
          , display_name = v_display_name
          , is_primary = v_is_primary
        WHERE
          channel_identifier_profile_id =  v_channel_identifier_profile_id
        ;
        IF NOT FOUND THEN
          RETURN QUERY SELECT FALSE, 'channel_identifier_profile_id does not exist'::TEXT;
        ELSE
          RETURN  QUERY  SELECT  TRUE::BOOLEAN, ''::TEXT;
        END IF;
      END IF;
  END;
  $$;


--
-- Name: FUNCTION f_channel_identifier_profile_update(v_channel_identifier_profile_id bigint, v_effective_from timestamp without time zone, v_effective_to timestamp without time zone, v_update_user_id integer, v_is_primary boolean, v_display_name text); Type: COMMENT; Schema: profile; Owner: -
--

COMMENT ON FUNCTION f_channel_identifier_profile_update(v_channel_identifier_profile_id bigint, v_effective_from timestamp without time zone, v_effective_to timestamp without time zone, v_update_user_id integer, v_is_primary boolean, v_display_name text) IS 'Updates a Channel Identifer/Profile association';


--
-- Name: f_client_select_by_profile_id(bigint); Type: FUNCTION; Schema: profile; Owner: -
--

CREATE FUNCTION f_client_select_by_profile_id(v_profile_id bigint) RETURNS integer
    LANGUAGE plpgsql IMMUTABLE
    AS $$
DECLARE
  v_client_id profile.profile.client_id%TYPE;
BEGIN
  SELECT p.client_id
  INTO v_client_id
  FROM profile.profile p
  WHERE p.profile_id =v_profile_id;
  RETURN v_client_id;
END;
$$;


--
-- Name: f_profile_attribute_delete(integer, character varying); Type: FUNCTION; Schema: profile; Owner: -
--

CREATE FUNCTION f_profile_attribute_delete(v_client_id integer, v_attribute_name character varying) RETURNS TABLE(succeeded boolean, message text)
    LANGUAGE plpgsql
    AS $$
DECLARE v_attribute_id INT;
BEGIN
  SELECT profile_attribute_id
  INTO v_attribute_id
    FROM profile.profile_attribute pa WHERE pa.name = v_attribute_name AND pa.client_id = v_client_id;
  IF v_attribute_id IS NULL
    THEN RETURN QUERY
    SELECT FALSE, 'attribute does not exist'::TEXT;
  ELSEIF EXISTS (
    SELECT 1 FROM profile.profile_attribute_value pav WHERE v_attribute_id = pav.profile_attribute_id
    )
    THEN  RETURN QUERY
    SELECT FALSE, 'attribute is in use'::TEXT;
  ELSE
    DELETE FROM profile.profile_attribute WHERE v_attribute_id = profile_attribute_id;
    RETURN QUERY
      SELECT TRUE, ''::TEXT;
  END IF;
END;
$$;


--
-- Name: f_profile_attribute_insert(integer, json, integer); Type: FUNCTION; Schema: profile; Owner: -
--

CREATE FUNCTION f_profile_attribute_insert(v_client_id integer, v_custom_attributes json, v_update_user_id integer) RETURNS TABLE(name text, succeeded boolean, message text)
    LANGUAGE plpgsql
    AS $$
BEGIN
  CREATE TEMP TABLE attributes (name TEXT PRIMARY KEY , description TEXT);

  INSERT INTO attributes(name, description)
  SELECT
    x."name",
    x.description
  FROM json_to_recordset(v_custom_attributes) AS x("name" TEXT, "description" TEXT);

  CREATE TEMP TABLE cte_output  (name TEXT PRIMARY KEY);

  WITH ins AS (
  INSERT INTO profile.profile_attribute (name,client_id,description,update_user_id)
  SELECT
     a."name",
     v_client_id,
     a.description,
     v_update_user_id
   FROM attributes as a
  ON CONFLICT DO NOTHING
  RETURNING  profile_attribute.name
  )
    INSERT INTO cte_output (name)
    SELECT ins.name FROM ins;

  RETURN  QUERY
    SELECT a.name,
      CASE  WHEN co.name = a.name THEN TRUE
            ELSE FALSE END as succeeded,
      CASE  WHEN co.name = a.name THEN ''::TEXT
            ELSE 'attribute already exists'::TEXT END as message
    FROM attributes a
    LEFT OUTER JOIN cte_output co ON co.name = a.name
    ;
  DROP TABLE attributes, cte_output;
END;
$$;


--
-- Name: f_profile_attribute_select(integer); Type: FUNCTION; Schema: profile; Owner: -
--

CREATE FUNCTION f_profile_attribute_select(v_client_id integer) RETURNS TABLE(profile_attribute_id integer, name character varying, description text)
    LANGUAGE plpgsql IMMUTABLE
    AS $$
BEGIN
  RETURN  QUERY
    SELECT
      pa.profile_attribute_id,
      pa.name,
      pa.description
    FROM profile.profile_attribute pa
    WHERE pa.client_id = v_client_id ;

END;
$$;


--
-- Name: f_profile_attribute_update(integer, json, integer); Type: FUNCTION; Schema: profile; Owner: -
--

CREATE FUNCTION f_profile_attribute_update(v_client_id integer, v_custom_attributes json, v_update_user_id integer) RETURNS TABLE(profile_attribute_id integer, succeeded boolean, message text)
    LANGUAGE plpgsql
    AS $$
BEGIN
  CREATE TEMP TABLE attributes (attribute_id INT PRIMARY KEY , name TEXT, description TEXT, is_duplicate BOOLEAN);
  INSERT  INTO attributes(attribute_id,name,description, is_duplicate)
    SELECT
      x."profileAttributeId"::INT,
      x."name",
      x.description,
      FALSE as is_duplicate
    FROM json_to_recordset(v_custom_attributes) AS x("name" TEXT, "description" TEXT, "profileAttributeId" INT);

    CREATE TEMP TABLE cte_output  (attribute_id INT PRIMARY KEY);

    UPDATE attributes
      SET is_duplicate = TRUE
    FROM profile.profile_attribute AS pa
    WHERE pa.name = attributes.name
      AND pa.profile_attribute_id != attributes.attribute_id
      AND pa.client_id = v_client_id
      AND pa.profile_attribute_id NOT IN (
        SELECT a2.attribute_id
        FROM attributes AS a2
        WHERE a2.name != pa.name
      )
    ;

  WITH ins AS (
    UPDATE  profile.profile_attribute SET
        name = a."name"
      ,description =  a.description
      ,update_user_id = v_update_user_id
      ,update_date = timezone('utc'::text, now())
    FROM attributes as a
      WHERE a.attribute_id = profile_attribute.profile_attribute_id
      AND client_id = v_client_id
      AND is_duplicate IS FALSE
  RETURNING  profile_attribute.profile_attribute_id
  )
    INSERT INTO cte_output (attribute_id)
    SELECT ins.profile_attribute_id FROM ins;

  RETURN  QUERY
    SELECT a.attribute_id,
      CASE  WHEN co.attribute_id = a.attribute_id THEN TRUE
            ELSE FALSE END as succeeded,
      CASE  WHEN co.attribute_id = a.attribute_id THEN ''::TEXT
            WHEN a.is_duplicate IS TRUE THEN  'attribute already exists'::TEXT
            ELSE 'attribute does not exist'::TEXT  END as message
    FROM attributes a
    LEFT OUTER JOIN cte_output co ON co.attribute_id= a.attribute_id
    ;
  DROP TABLE attributes, cte_output;
END;
$$;


--
-- Name: f_profile_attribute_value_delete(bigint, json); Type: FUNCTION; Schema: profile; Owner: -
--

CREATE FUNCTION f_profile_attribute_value_delete(v_profile_id bigint, v_custom_attributes json) RETURNS integer
    LANGUAGE plpgsql
    AS $$
DECLARE v_client_id INT;
    num_rows INT;
BEGIN
  SELECT p.client_id
    INTO v_client_id
  FROM profile.profile p
  WHERE p.profile_id =v_profile_id;

  CREATE TEMP TABLE IF NOT EXISTS attributes (name TEXT PRIMARY KEY , attribute_id INT);

  INSERT INTO attributes(name)
  SELECT
    *
  FROM json_array_elements_text(v_custom_attributes) AS x;

  UPDATE attributes SET
      attribute_id = pa.profile_attribute_id
  FROM profile.profile_attribute pa
  WHERE pa.client_id= v_client_id
    AND pa.name = attributes.name;

  DELETE FROM profile.profile_attribute_value AS  pav
  WHERE pav.profile_attribute_id IN (SELECT attribute_id FROM attributes)
  AND pav.profile_id = v_profile_id;

  IF FOUND THEN
    GET DIAGNOSTICS num_rows = ROW_COUNT;
  END IF;
  DROP TABLE attributes;
  RETURN coalesce(num_rows,0);
END;
$$;


--
-- Name: f_profile_attribute_value_insert(bigint, json, integer); Type: FUNCTION; Schema: profile; Owner: -
--

CREATE FUNCTION f_profile_attribute_value_insert(v_profile_id bigint, v_custom_attributes json, v_update_user_id integer) RETURNS TABLE(name text, succeeded boolean, message text)
    LANGUAGE plpgsql
    AS $$
DECLARE v_client_id INT;
BEGIN
  SELECT p.client_id
    INTO v_client_id
  FROM profile.profile p
  WHERE p.profile_id =v_profile_id;

  CREATE TEMP TABLE attributes (name TEXT PRIMARY KEY , value TEXT, attribute_id INT);

  INSERT INTO attributes(name, value)
  SELECT
    x."name",
    x.value
  FROM json_to_recordset(v_custom_attributes) AS x("name" TEXT, "value" TEXT);

  UPDATE attributes SET
      attribute_id = pa.profile_attribute_id
  FROM profile.profile_attribute pa
  WHERE pa.client_id= v_client_id
    AND pa.name = attributes.name;

  CREATE TEMP TABLE cte_output  (name TEXT PRIMARY KEY);

  WITH ins AS (
  INSERT INTO profile.profile_attribute_value(profile_attribute_id, profile_id, value, update_user_id)
    SELECT a.attribute_id
    , v_profile_id
    , a.value
    , v_update_user_id
   FROM attributes as a
    WHERE a.attribute_id IS NOT NULL
  ON CONFLICT DO NOTHING
  RETURNING  profile_attribute_value.profile_attribute_id
  )
    INSERT INTO cte_output (name)
    SELECT a.name
    FROM ins
    JOIN attributes a ON attribute_id = ins.profile_attribute_id;

  RETURN  QUERY
    SELECT a.name,
      CASE  WHEN a.name = co.name THEN TRUE
            WHEN  a.attribute_id IS NULL THEN FALSE
            WHEN co.name IS NULL THEN FALSE
            ELSE FALSE END as succeeded,
      CASE  WHEN a.name = co.name THEN ''::TEXT
            WHEN  a.attribute_id IS NULL THEN 'no profile attribute id found'::TEXT
            WHEN co.name IS NULL THEN 'profile attribute already exists for this client'::TEXT
            ELSE 'unknown error'::TEXT END as message
    FROM attributes a
    LEFT OUTER JOIN cte_output co ON co.name = a.name
    ;
  DROP TABLE attributes, cte_output;
END;
$$;


--
-- Name: f_profile_attribute_value_update(bigint, json, integer); Type: FUNCTION; Schema: profile; Owner: -
--

CREATE FUNCTION f_profile_attribute_value_update(v_profile_id bigint, v_custom_attributes json, v_update_user_id integer) RETURNS TABLE(name text, succeeded boolean, message text)
    LANGUAGE plpgsql
    AS $$
DECLARE
  v_client_id profile.profile.client_id%TYPE;
BEGIN
  SELECT v.client_id INTO v_client_id FROM profile.f_client_select_by_profile_id(v_profile_id) as v(client_id);

  CREATE TEMP TABLE attributes  (attribute_id INT , name TEXT PRIMARY KEY , value TEXT);
  INSERT  INTO attributes(attribute_id,name,value)--,has_description, is_duplicate)
    SELECT
      pa.profile_attribute_id,
      x."name",
      x.value
    FROM json_to_recordset(v_custom_attributes) AS x("name" TEXT, "value" TEXT)
    LEFT OUTER JOIN profile.profile_attribute pa
      ON pa.name = x.name
      AND v_client_id = pa.client_id;

  CREATE TEMP TABLE cte_output  (attribute_id INT PRIMARY KEY);
  WITH ins AS (
    UPDATE profile.profile_attribute_value as pav SET
      value = a.value,
      update_date = timezone('utc'::text, now()),
      update_user_id = v_update_user_id
    FROM attributes as a
      WHERE a.attribute_id = pav.profile_attribute_id
        AND v_profile_id = pav.profile_id
    RETURNING  pav.profile_attribute_id
  )
    INSERT INTO cte_output (attribute_id)
    SELECT ins.profile_attribute_id FROM ins;

  RETURN  QUERY
    SELECT a.name,
      CASE  WHEN co.attribute_id = a.attribute_id THEN TRUE
            WHEN a.attribute_id IS NULL THEN FALSE --attribute not found for client
            WHEN co.attribute_id IS NULL AND a.attribute_id IS NOT NULL THEN FALSE --attribute not found for profile
            ELSE FALSE END as succeeded, --unknown
      CASE  WHEN co.attribute_id = a.attribute_id THEN ''::TEXT
            WHEN a.attribute_id IS NULL THEN 'attribute not found for client'::TEXT
            WHEN co.attribute_id IS NULL AND a.attribute_id IS  NOT  NULL THEN 'attribute not found for profile'
            ELSE 'unknown error'::TEXT  END as message
    FROM attributes a
    LEFT OUTER JOIN cte_output co ON co.attribute_id= a.attribute_id
    ;

  DROP TABLE IF EXISTS attributes, cte_output;
END;
$$;


--
-- Name: f_profile_delete(bigint); Type: FUNCTION; Schema: profile; Owner: -
--

CREATE FUNCTION f_profile_delete(v_profile_id bigint) RETURNS TABLE(succeeded boolean, message text)
    LANGUAGE plpgsql
    AS $$
  BEGIN
    DELETE FROM profile.profile
      WHERE profile.profile_id = v_profile_id
    ;
    IF FOUND IS FALSE THEN RETURN  QUERY
      SELECT  FALSE,'profile does not exist'::TEXT;
    ELSE RETURN QUERY
      SELECT  TRUE ,''::TEXT;
    END IF ;
  END;
  $$;


--
-- Name: FUNCTION f_profile_delete(v_profile_id bigint); Type: COMMENT; Schema: profile; Owner: -
--

COMMENT ON FUNCTION f_profile_delete(v_profile_id bigint) IS 'Deletes a profile';


--
-- Name: f_profile_delete(bigint, integer); Type: FUNCTION; Schema: profile; Owner: -
--

CREATE FUNCTION f_profile_delete(v_profile_id bigint, v_update_user_id integer) RETURNS TABLE(succeeded boolean, message text)
    LANGUAGE plpgsql
    AS $$
  BEGIN

    DELETE FROM profile.profile
      WHERE profile.profile_id = v_profile_id
    ;
    IF FOUND IS FALSE THEN RETURN  QUERY
      SELECT  FALSE,'profile does not exist'::TEXT;
    ELSE RETURN QUERY
      SELECT  TRUE ,''::TEXT;
    END IF ;
  END;
  $$;


--
-- Name: f_profile_insert(integer, text, text, integer, character varying, integer, text, smallint, boolean, character varying); Type: FUNCTION; Schema: profile; Owner: -
--

CREATE FUNCTION f_profile_insert(v_client_id integer, v_first_name text, v_last_name text, v_user_id integer, v_external_facing_id character varying, v_update_user_id integer, v_organization_name text, v_profile_type_id smallint, v_is_internal boolean, v_time_zone_abbrev character varying) RETURNS TABLE(profile_id bigint, succeeded boolean, message text)
    LANGUAGE plpgsql
    AS $$
    DECLARE out_profile_id BIGINT;
  BEGIN
    INSERT  INTO profile.profile (client_id, first_name, last_name, user_id, external_facing_id,  update_user_id, organization_name, profile_type_id, is_internal, time_zone_abbrev)
    VALUES (v_client_id,v_first_name,v_last_name,v_user_id,v_external_facing_id,v_update_user_id,v_organization_name, v_profile_type_id, v_is_internal, v_time_zone_abbrev)
      ON CONFLICT DO NOTHING
    RETURNING profile.profile.profile_id INTO out_profile_id;
    IF out_profile_id IS NULL
      THEN RETURN QUERY
        SELECT out_profile_id, FALSE , 'could not create profile'::TEXT;
     ELSE RETURN  QUERY
        SELECT out_profile_id, TRUE ,''::TEXT;
      END IF ;
  END;
  $$;


--
-- Name: FUNCTION f_profile_insert(v_client_id integer, v_first_name text, v_last_name text, v_user_id integer, v_external_facing_id character varying, v_update_user_id integer, v_organization_name text, v_profile_type_id smallint, v_is_internal boolean, v_time_zone_abbrev character varying); Type: COMMENT; Schema: profile; Owner: -
--

COMMENT ON FUNCTION f_profile_insert(v_client_id integer, v_first_name text, v_last_name text, v_user_id integer, v_external_facing_id character varying, v_update_user_id integer, v_organization_name text, v_profile_type_id smallint, v_is_internal boolean, v_time_zone_abbrev character varying) IS 'Creates a new profile record';


--
-- Name: f_profile_insert(integer, text, text, integer, character varying, integer, text, smallint, boolean, character varying, json); Type: FUNCTION; Schema: profile; Owner: -
--

CREATE FUNCTION f_profile_insert(v_client_id integer, v_first_name text, v_last_name text, v_user_id integer, v_external_facing_id character varying, v_update_user_id integer, v_organization_name text, v_profile_type_id smallint, v_is_internal boolean, v_time_zone_abbrev character varying, v_custom_attributes json) RETURNS TABLE(profile_id bigint, succeeded boolean, message text)
    LANGUAGE plpgsql
    AS $$
    DECLARE out_profile_id BIGINT;
  BEGIN

    CREATE TEMP TABLE profile_create_output AS
    SELECT ins.profile_id, ins.succeeded, ins.message
    FROM profile.f_profile_insert (
     v_client_id
    ,v_first_name
    ,v_last_name
    ,v_user_id
    ,v_external_facing_id
    ,v_update_user_id
    ,v_organization_name
    ,v_profile_type_id
    ,v_is_internal
    ,v_time_zone_abbrev
    ) AS ins;

    SELECT o.profile_id INTO out_profile_id
      FROM profile_create_output o WHERE o.succeeded IS TRUE;
    IF (out_profile_id IS NOT NULL)
      THEN
      CREATE TEMP TABLE attribute_create_output AS
      SELECT att."name", att.succeeded, att."message"
      FROM profile.f_profile_attribute_value_insert(out_profile_id,v_custom_attributes,v_update_user_id) as att;
      --check if attributes were created successfully

      IF EXISTS (SELECT  * FROM attribute_create_output att WHERE att.succeeded IS FALSE)
        THEN
          RAISE EXCEPTION USING ERRCODE ='ATTFA';
        ELSE
          --return success
          RETURN QUERY  SELECT out_profile_id, TRUE ,''::TEXT;
          DROP TABLE IF EXISTS  attribute_create_output,profile_create_output;
      END IF;
    --return failure
    ELSE
      RETURN QUERY SELECT  o.profile_id, o.succeeded, o.message  FROM profile_create_output AS o;
      DROP TABLE IF EXISTS  attribute_create_output,profile_create_output;
    END IF;

      EXCEPTION  WHEN SQLSTATE 'ATTFA' THEN
          --return failure
          RETURN QUERY  SELECT null::bigint, false, 'attributes are not valid'::TEXT;
  END;
  $$;


--
-- Name: FUNCTION f_profile_insert(v_client_id integer, v_first_name text, v_last_name text, v_user_id integer, v_external_facing_id character varying, v_update_user_id integer, v_organization_name text, v_profile_type_id smallint, v_is_internal boolean, v_time_zone_abbrev character varying, v_custom_attributes json); Type: COMMENT; Schema: profile; Owner: -
--

COMMENT ON FUNCTION f_profile_insert(v_client_id integer, v_first_name text, v_last_name text, v_user_id integer, v_external_facing_id character varying, v_update_user_id integer, v_organization_name text, v_profile_type_id smallint, v_is_internal boolean, v_time_zone_abbrev character varying, v_custom_attributes json) IS 'Creates a new profile record with new attribute values';


--
-- Name: f_profile_select(bigint); Type: FUNCTION; Schema: profile; Owner: -
--

CREATE FUNCTION f_profile_select(v_profile_id bigint) RETURNS TABLE(client_id integer, first_name text, last_name text, user_id integer, external_facing_id character varying, organization_name text, profile_type_id smallint, is_internal boolean, time_zone_abbrev character varying, custom_attributes json)
    LANGUAGE plpgsql IMMUTABLE
    AS $$
  DECLARE
    v_custom_attributes JSON;
  BEGIN
      SELECT json_agg(json_build_object('profileAttributeId', pa.profile_attribute_id,'name',pa.name,'value',pav.value))
      INTO v_custom_attributes
      FROM profile.profile_attribute pa
      JOIN profile.profile_attribute_value pav
        ON pa.profile_attribute_id = pav.profile_attribute_id
      WHERE v_profile_id = pav.profile_id;

      RETURN QUERY SELECT
         p.client_id
        ,p.first_name
        ,p.last_name
        ,p.user_id
        ,p.external_facing_id
        ,p.organization_name
        ,p.profile_type_id
        ,p.is_internal
        ,p.time_zone_abbrev
        ,v_custom_attributes
        FROM profile.profile p
        WHERE p.profile_id = v_profile_id
    ;
  END;
  $$;


--
-- Name: FUNCTION f_profile_select(v_profile_id bigint); Type: COMMENT; Schema: profile; Owner: -
--

COMMENT ON FUNCTION f_profile_select(v_profile_id bigint) IS 'Gets a profile info by ID with all attributes';


--
-- Name: f_profile_update(bigint, text, text, integer, character varying, integer, text, smallint, boolean, character varying, json); Type: FUNCTION; Schema: profile; Owner: -
--

CREATE FUNCTION f_profile_update(v_profile_id bigint, v_first_name text, v_last_name text, v_user_id integer, v_external_facing_id character varying, v_update_user_id integer, v_organization_name text, v_profile_type_id smallint, v_is_internal boolean, v_time_zone_abbrev character varying, v_custom_attributes json) RETURNS TABLE(succeeded boolean, message text)
    LANGUAGE plpgsql
    AS $$
  DECLARE
      v_client_id INT;
    n INT;
  BEGIN
    SELECT v.client_id INTO v_client_id FROM profile.f_client_select_by_profile_id(v_profile_id) as v(client_id);
    IF EXISTS (
      SELECT
          1
        FROM json_to_recordset(v_custom_attributes) AS x("name" TEXT, "value" TEXT)
        LEFT OUTER JOIN profile.profile_attribute pa
          ON pa.name = x.name
          AND v_client_id = pa.client_id
        WHERE pa.profile_attribute_id IS NULL
      ) THEN --EXIT
        RETURN QUERY SELECT false, 'attributes are not valid'::TEXT;
    ELSE --WE HAVE VALID ATTRIBUTES FOR THE CLIENT, CONTINUE
      UPDATE profile.profile SET
        first_name = v_first_name,
        last_name = v_last_name,
        external_facing_id = v_external_facing_id,
        organization_name = v_organization_name,
        profile_type_id = v_profile_type_id,
        is_internal = v_is_internal,
        time_zone_abbrev = v_time_zone_abbrev,
        user_id = v_user_id,
        update_user_id = v_update_user_id,
        update_date = timezone('utc'::text, now())
      WHERE  profile.profile_id = v_profile_id
        AND profile.client_id = v_client_id;
      IF FOUND IS FALSE THEN --EXIT
        RETURN QUERY SELECT FALSE,'profile does not exist'::TEXT;
      ELSE --OUR UPDATE WAS SUCCESSFUL, CONTINUE
        IF v_custom_attributes IS NOT NULL THEN
          --INSERT
          INSERT INTO profile.profile_attribute_value as pav (profile_attribute_id, profile_id, value, update_user_id)
            SELECT pa.profile_attribute_id
              , v_profile_id
              , x.value
              , v_update_user_id
            FROM json_to_recordset(v_custom_attributes) AS x("name" TEXT, "value" TEXT)
            INNER JOIN profile.profile_attribute pa
              ON pa.name = x.name
              AND v_client_id = pa.client_id
          ON CONFLICT (profile_id,profile_attribute_id) DO UPDATE SET value = EXCLUDED.value
          WHERE pav.profile_attribute_id = EXCLUDED.profile_attribute_id
            AND pav.profile_id = v_profile_id;
          --DELETE
          DELETE FROM profile.profile_attribute_value as pav
          WHERE pav.profile_id = v_profile_id
            AND pav.profile_attribute_id NOT IN (
            SELECT
             pa.profile_attribute_id as attribute_id
            FROM json_to_recordset(v_custom_attributes) AS x("name" TEXT, "value" TEXT)
            INNER JOIN profile.profile_attribute pa
              ON pa.name = x.name
              AND v_client_id = pa.client_id
          );
        END IF;
      RETURN QUERY SELECT TRUE ,''::TEXT;
      END IF ;
    END IF ;
  END;
  $$;


--
-- Name: FUNCTION f_profile_update(v_profile_id bigint, v_first_name text, v_last_name text, v_user_id integer, v_external_facing_id character varying, v_update_user_id integer, v_organization_name text, v_profile_type_id smallint, v_is_internal boolean, v_time_zone_abbrev character varying, v_custom_attributes json); Type: COMMENT; Schema: profile; Owner: -
--

COMMENT ON FUNCTION f_profile_update(v_profile_id bigint, v_first_name text, v_last_name text, v_user_id integer, v_external_facing_id character varying, v_update_user_id integer, v_organization_name text, v_profile_type_id smallint, v_is_internal boolean, v_time_zone_abbrev character varying, v_custom_attributes json) IS 'Updates a profile record';


--
-- Name: f_profile_update_user(bigint, integer, integer); Type: FUNCTION; Schema: profile; Owner: -
--

CREATE FUNCTION f_profile_update_user(v_profile_id bigint, v_user_id integer, v_update_user_id integer) RETURNS TABLE(succeeded boolean, message text)
    LANGUAGE plpgsql
    AS $$
  BEGIN
    UPDATE profile.profile SET user_id = v_user_id
        , update_user_id = v_update_user_id
        , update_date = timezone('utc'::text, now())
    WHERE  profile.profile_id = v_profile_id
      AND NOT EXISTS (  SELECT 1
                        FROM profile.profile p
                        WHERE p.user_id = v_user_id
                          AND p.profile_id != v_profile_id
                      )
    ;
    IF FOUND IS FALSE THEN
      IF EXISTS (SELECT 1 FROM profile.profile p WHERE p.profile_id = v_profile_id) THEN
        RETURN  QUERY
        SELECT FALSE,'user is already associated with another profile'::TEXT;
      ELSE
        RETURN  QUERY
        SELECT FALSE,'profile does not exist'::TEXT;
      END IF;
    ELSE RETURN QUERY
      SELECT TRUE ,''::TEXT;
    END IF ;
  END;
  $$;


--
-- Name: FUNCTION f_profile_update_user(v_profile_id bigint, v_user_id integer, v_update_user_id integer); Type: COMMENT; Schema: profile; Owner: -
--

COMMENT ON FUNCTION f_profile_update_user(v_profile_id bigint, v_user_id integer, v_update_user_id integer) IS 'Adds or removes the user_id associated with a profile';


--
-- Name: trf_channel_identifier_profile(); Type: FUNCTION; Schema: profile; Owner: -
--

CREATE FUNCTION trf_channel_identifier_profile() RETURNS trigger
    LANGUAGE plpgsql IMMUTABLE
    AS $$

BEGIN
    IF EXISTS(
        SELECT 1
        FROM profile.channel_identifier_profile AS cip
        WHERE 	
				cip.client_id = NEW.client_id
			AND	cip.channel_identifier_id = NEW.channel_identifier_id
      AND cip.channel_identifier_profile_id != NEW.channel_identifier_profile_id
			AND		--FIND EFFECTIVE DATES THAT OVERLAP
              (TSRANGE(cip.effective_from, cip.effective_to) &&
               TSRANGE(NEW.effective_from, NEW.effective_to)) = TRUE :: BOOL --date ranges overlap
    )
    THEN RAISE EXCEPTION 'time periods overlap with existing profile';
    END IF;
    RETURN NULL;
END;
$$;


SET search_path = channel, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: channel; Type: TABLE; Schema: channel; Owner: -
--

CREATE TABLE channel (
    channel_id integer NOT NULL,
    name text NOT NULL,
    display_name text,
    content_type_id integer NOT NULL
);


--
-- Name: channel_channel_id_seq; Type: SEQUENCE; Schema: channel; Owner: -
--

CREATE SEQUENCE channel_channel_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: channel_channel_id_seq; Type: SEQUENCE OWNED BY; Schema: channel; Owner: -
--

ALTER SEQUENCE channel_channel_id_seq OWNED BY channel.channel_id;


--
-- Name: channel_identifier; Type: TABLE; Schema: channel; Owner: -
--

CREATE TABLE channel_identifier (
    channel_identifier_id bigint NOT NULL,
    channel_id integer NOT NULL,
    identifier text NOT NULL,
    update_date timestamp without time zone DEFAULT timezone('utc'::text, now()) NOT NULL,
    update_user_id integer NOT NULL
);


--
-- Name: channel_identifier_channel_identifier_id_seq; Type: SEQUENCE; Schema: channel; Owner: -
--

CREATE SEQUENCE channel_identifier_channel_identifier_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: channel_identifier_channel_identifier_id_seq; Type: SEQUENCE OWNED BY; Schema: channel; Owner: -
--

ALTER SEQUENCE channel_identifier_channel_identifier_id_seq OWNED BY channel_identifier.channel_identifier_id;


--
-- Name: channel_identifier_type; Type: TABLE; Schema: channel; Owner: -
--

CREATE TABLE channel_identifier_type (
    channel_identifier_type_id integer NOT NULL,
    name character varying(255)
);


--
-- Name: TABLE channel_identifier_type; Type: COMMENT; Schema: channel; Owner: -
--

COMMENT ON TABLE channel_identifier_type IS 'User or group';


SET search_path = "group", pg_catalog;

--
-- Name: group_id_seq; Type: SEQUENCE; Schema: group; Owner: -
--

CREATE SEQUENCE group_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: group; Type: TABLE; Schema: group; Owner: -
--

CREATE TABLE "group" (
    group_id integer DEFAULT nextval('group_id_seq'::regclass) NOT NULL,
    group_type_id smallint NOT NULL,
    name text,
    client_id integer NOT NULL,
    update_user_id integer NOT NULL,
    update_date timestamp without time zone DEFAULT timezone('utc'::text, now()) NOT NULL,
    parent_group_id integer,
    source_id integer DEFAULT 1 NOT NULL,
    external_id text
);


--
-- Name: COLUMN "group".group_type_id; Type: COMMENT; Schema: group; Owner: -
--

COMMENT ON COLUMN "group".group_type_id IS '1=org/2=overlay';


--
-- Name: group_filter_id_seq; Type: SEQUENCE; Schema: group; Owner: -
--

CREATE SEQUENCE group_filter_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: group_filter; Type: TABLE; Schema: group; Owner: -
--

CREATE TABLE group_filter (
    group_filter_id integer DEFAULT nextval('group_filter_id_seq'::regclass) NOT NULL,
    channel_id bigint NOT NULL,
    group_id integer NOT NULL,
    update_user_id integer NOT NULL,
    update_date timestamp without time zone DEFAULT timezone('utc'::text, now()) NOT NULL
);


--
-- Name: group_filter_history; Type: TABLE; Schema: group; Owner: -
--

CREATE TABLE group_filter_history (
    history_id integer NOT NULL,
    operation character(1) NOT NULL,
    group_filter_id integer NOT NULL,
    channel_id bigint NOT NULL,
    group_id integer NOT NULL,
    update_user_id integer NOT NULL,
    update_date timestamp without time zone NOT NULL
);


--
-- Name: group_filter_history_history_id_seq; Type: SEQUENCE; Schema: group; Owner: -
--

CREATE SEQUENCE group_filter_history_history_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: group_filter_history_history_id_seq; Type: SEQUENCE OWNED BY; Schema: group; Owner: -
--

ALTER SEQUENCE group_filter_history_history_id_seq OWNED BY group_filter_history.history_id;


--
-- Name: group_history; Type: TABLE; Schema: group; Owner: -
--

CREATE TABLE group_history (
    history_id integer NOT NULL,
    operation character(1) NOT NULL,
    group_id integer NOT NULL,
    group_type_id smallint NOT NULL,
    name text,
    client_id integer NOT NULL,
    update_user_id integer NOT NULL,
    update_date timestamp without time zone NOT NULL,
    parent_group_id integer,
    source_id integer NOT NULL,
    external_id text
);


--
-- Name: group_history_history_id_seq; Type: SEQUENCE; Schema: group; Owner: -
--

CREATE SEQUENCE group_history_history_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: group_history_history_id_seq; Type: SEQUENCE OWNED BY; Schema: group; Owner: -
--

ALTER SEQUENCE group_history_history_id_seq OWNED BY group_history.history_id;


--
-- Name: group_permission_id_seq; Type: SEQUENCE; Schema: group; Owner: -
--

CREATE SEQUENCE group_permission_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: group_permission; Type: TABLE; Schema: group; Owner: -
--

CREATE TABLE group_permission (
    group_permission_id integer DEFAULT nextval('group_permission_id_seq'::regclass) NOT NULL,
    user_id bigint NOT NULL,
    update_user_id integer NOT NULL,
    update_date timestamp without time zone DEFAULT timezone('utc'::text, now()) NOT NULL,
    group_id integer NOT NULL
);


--
-- Name: group_permission_history; Type: TABLE; Schema: group; Owner: -
--

CREATE TABLE group_permission_history (
    history_id integer NOT NULL,
    operation character(1) NOT NULL,
    group_permission_id integer NOT NULL,
    user_id bigint NOT NULL,
    update_user_id integer NOT NULL,
    update_date timestamp without time zone NOT NULL,
    group_id integer NOT NULL
);


--
-- Name: group_permission_history_history_id_seq; Type: SEQUENCE; Schema: group; Owner: -
--

CREATE SEQUENCE group_permission_history_history_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: group_permission_history_history_id_seq; Type: SEQUENCE OWNED BY; Schema: group; Owner: -
--

ALTER SEQUENCE group_permission_history_history_id_seq OWNED BY group_permission_history.history_id;


SET search_path = profile, pg_catalog;

--
-- Name: profile; Type: TABLE; Schema: profile; Owner: -
--

CREATE TABLE profile (
    profile_id bigint NOT NULL,
    client_id integer NOT NULL,
    first_name text,
    last_name text,
    user_id integer,
    external_facing_id character varying(100),
    update_date timestamp without time zone DEFAULT timezone('utc'::text, now()) NOT NULL,
    update_user_id integer NOT NULL,
    organization_name text,
    profile_type_id smallint NOT NULL,
    is_internal boolean,
    time_zone_abbrev character varying(6)
);


--
-- Name: COLUMN profile.external_facing_id; Type: COMMENT; Schema: profile; Owner: -
--

COMMENT ON COLUMN profile.external_facing_id IS 'This is the ID that we give to the client so they can track this record in there system. We may want to move this to the profile_source table if we see the need to have multiple depending on the source we are syncing.';


--
-- Name: profile_profile_id_seq; Type: SEQUENCE; Schema: profile; Owner: -
--

CREATE SEQUENCE profile_profile_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: profile_profile_id_seq; Type: SEQUENCE OWNED BY; Schema: profile; Owner: -
--

ALTER SEQUENCE profile_profile_id_seq OWNED BY profile.profile_id;


SET search_path = "group", pg_catalog;

--
-- Name: group_profile; Type: TABLE; Schema: group; Owner: -
--

CREATE TABLE group_profile (
    group_profile_id integer NOT NULL,
    profile_id bigint DEFAULT nextval('profile.profile_profile_id_seq'::regclass) NOT NULL,
    group_id integer NOT NULL,
    update_user_id integer NOT NULL,
    update_date timestamp without time zone DEFAULT timezone('utc'::text, now()) NOT NULL
);


--
-- Name: group_profile_group_profile_id_seq; Type: SEQUENCE; Schema: group; Owner: -
--

CREATE SEQUENCE group_profile_group_profile_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: group_profile_group_profile_id_seq; Type: SEQUENCE OWNED BY; Schema: group; Owner: -
--

ALTER SEQUENCE group_profile_group_profile_id_seq OWNED BY group_profile.group_profile_id;


--
-- Name: group_profile_history; Type: TABLE; Schema: group; Owner: -
--

CREATE TABLE group_profile_history (
    history_id integer NOT NULL,
    operation character(1) NOT NULL,
    group_profile_id integer NOT NULL,
    profile_id bigint NOT NULL,
    group_id integer NOT NULL,
    update_user_id integer NOT NULL,
    update_date timestamp without time zone NOT NULL
);


--
-- Name: group_profile_history_history_id_seq; Type: SEQUENCE; Schema: group; Owner: -
--

CREATE SEQUENCE group_profile_history_history_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: group_profile_history_history_id_seq; Type: SEQUENCE OWNED BY; Schema: group; Owner: -
--

ALTER SEQUENCE group_profile_history_history_id_seq OWNED BY group_profile_history.history_id;


--
-- Name: group_relationship; Type: TABLE; Schema: group; Owner: -
--

CREATE TABLE group_relationship (
    group_relationship_id integer NOT NULL,
    group_id integer NOT NULL,
    relationship_type_id smallint NOT NULL,
    related_group_id integer,
    update_user_id integer NOT NULL,
    update_date timestamp without time zone DEFAULT timezone('utc'::text, now()) NOT NULL
);


--
-- Name: group_relationship_group_relationship_id_seq; Type: SEQUENCE; Schema: group; Owner: -
--

CREATE SEQUENCE group_relationship_group_relationship_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: group_relationship_group_relationship_id_seq; Type: SEQUENCE OWNED BY; Schema: group; Owner: -
--

ALTER SEQUENCE group_relationship_group_relationship_id_seq OWNED BY group_relationship.group_relationship_id;


--
-- Name: group_relationship_history; Type: TABLE; Schema: group; Owner: -
--

CREATE TABLE group_relationship_history (
    history_id integer NOT NULL,
    operation character(1) NOT NULL,
    update_date timestamp without time zone NOT NULL,
    group_relationship_id integer NOT NULL,
    group_id integer NOT NULL,
    relationship_type_id smallint NOT NULL,
    related_group_id integer,
    update_user_id integer NOT NULL
);


--
-- Name: group_relationship_history_history_id_seq; Type: SEQUENCE; Schema: group; Owner: -
--

CREATE SEQUENCE group_relationship_history_history_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: group_relationship_history_history_id_seq; Type: SEQUENCE OWNED BY; Schema: group; Owner: -
--

ALTER SEQUENCE group_relationship_history_history_id_seq OWNED BY group_relationship_history.history_id;


--
-- Name: group_type; Type: TABLE; Schema: group; Owner: -
--

CREATE TABLE group_type (
    group_type_id smallint NOT NULL,
    name text NOT NULL
);


--
-- Name: relationship_tree; Type: TABLE; Schema: group; Owner: -
--

CREATE TABLE relationship_tree (
    relationship_tree_id integer NOT NULL,
    group_relationship_id integer,
    path public.ltree NOT NULL
);


--
-- Name: TABLE relationship_tree; Type: COMMENT; Schema: group; Owner: -
--

COMMENT ON TABLE relationship_tree IS 'Contains all paths that lead a group';


--
-- Name: relationship_tree_relationship_tree_id_seq; Type: SEQUENCE; Schema: group; Owner: -
--

CREATE SEQUENCE relationship_tree_relationship_tree_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: relationship_tree_relationship_tree_id_seq; Type: SEQUENCE OWNED BY; Schema: group; Owner: -
--

ALTER SEQUENCE relationship_tree_relationship_tree_id_seq OWNED BY relationship_tree.relationship_tree_id;


--
-- Name: relationship_type; Type: TABLE; Schema: group; Owner: -
--

CREATE TABLE relationship_type (
    relationship_type_id smallint NOT NULL,
    name text NOT NULL
);


--
-- Name: TABLE relationship_type; Type: COMMENT; Schema: group; Owner: -
--

COMMENT ON TABLE relationship_type IS '1=owner/2=link';


--
-- Name: v_client_id; Type: TABLE; Schema: group; Owner: -
--

CREATE TABLE v_client_id (
    client_id integer
);


SET search_path = profile, pg_catalog;

--
-- Name: channel_identifier_profile; Type: TABLE; Schema: profile; Owner: -
--

CREATE TABLE channel_identifier_profile (
    channel_identifier_profile_id bigint NOT NULL,
    effective_from timestamp without time zone,
    effective_to timestamp without time zone,
    update_date timestamp without time zone DEFAULT timezone('utc'::text, now()) NOT NULL,
    update_user_id integer NOT NULL,
    channel_identifier_id bigint NOT NULL,
    profile_id bigint NOT NULL,
    is_primary boolean NOT NULL,
    display_name text,
    client_id integer NOT NULL
);


--
-- Name: channel_identifier_profile_channel_identifier_profile_id_seq; Type: SEQUENCE; Schema: profile; Owner: -
--

CREATE SEQUENCE channel_identifier_profile_channel_identifier_profile_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: channel_identifier_profile_channel_identifier_profile_id_seq; Type: SEQUENCE OWNED BY; Schema: profile; Owner: -
--

ALTER SEQUENCE channel_identifier_profile_channel_identifier_profile_id_seq OWNED BY channel_identifier_profile.channel_identifier_profile_id;


--
-- Name: profile_attribute; Type: TABLE; Schema: profile; Owner: -
--

CREATE TABLE profile_attribute (
    profile_attribute_id integer NOT NULL,
    client_id integer NOT NULL,
    name character varying(255) NOT NULL,
    update_date timestamp without time zone DEFAULT timezone('utc'::text, now()) NOT NULL,
    update_user_id integer NOT NULL,
    description text
);


--
-- Name: profile_attribute_profile_attribute_id_seq; Type: SEQUENCE; Schema: profile; Owner: -
--

CREATE SEQUENCE profile_attribute_profile_attribute_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: profile_attribute_profile_attribute_id_seq; Type: SEQUENCE OWNED BY; Schema: profile; Owner: -
--

ALTER SEQUENCE profile_attribute_profile_attribute_id_seq OWNED BY profile_attribute.profile_attribute_id;


--
-- Name: profile_attribute_value; Type: TABLE; Schema: profile; Owner: -
--

CREATE TABLE profile_attribute_value (
    profile_attribute_value_id integer NOT NULL,
    profile_attribute_id integer NOT NULL,
    profile_id bigint NOT NULL,
    value character varying(255) NOT NULL,
    update_date timestamp without time zone DEFAULT timezone('utc'::text, now()) NOT NULL,
    update_user_id integer NOT NULL
);


--
-- Name: profile_attribute_value_profile_attribute_value_id_seq; Type: SEQUENCE; Schema: profile; Owner: -
--

CREATE SEQUENCE profile_attribute_value_profile_attribute_value_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: profile_attribute_value_profile_attribute_value_id_seq; Type: SEQUENCE OWNED BY; Schema: profile; Owner: -
--

ALTER SEQUENCE profile_attribute_value_profile_attribute_value_id_seq OWNED BY profile_attribute_value.profile_attribute_value_id;


--
-- Name: profile_type; Type: TABLE; Schema: profile; Owner: -
--

CREATE TABLE profile_type (
    profile_type_id smallint NOT NULL,
    name text NOT NULL
);


--
-- Name: TABLE profile_type; Type: COMMENT; Schema: profile; Owner: -
--

COMMENT ON TABLE profile_type IS 'Person or Organization
';


SET search_path = source, pg_catalog;

--
-- Name: channel_identifier_profile_source; Type: TABLE; Schema: source; Owner: -
--

CREATE TABLE channel_identifier_profile_source (
    channel_identifier_profile_source_id bigint NOT NULL,
    channel_identifier_profile_id bigint NOT NULL,
    source_id integer NOT NULL,
    external_id character varying(500) NOT NULL,
    update_date timestamp without time zone DEFAULT timezone('utc'::text, now()) NOT NULL,
    sync_date timestamp without time zone
);


--
-- Name: channel_identifier_profile_so_channel_identifier_profile_so_seq; Type: SEQUENCE; Schema: source; Owner: -
--

CREATE SEQUENCE channel_identifier_profile_so_channel_identifier_profile_so_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: channel_identifier_profile_so_channel_identifier_profile_so_seq; Type: SEQUENCE OWNED BY; Schema: source; Owner: -
--

ALTER SEQUENCE channel_identifier_profile_so_channel_identifier_profile_so_seq OWNED BY channel_identifier_profile_source.channel_identifier_profile_source_id;


--
-- Name: channel_identifier_source; Type: TABLE; Schema: source; Owner: -
--

CREATE TABLE channel_identifier_source (
    channel_identifier_source_id integer NOT NULL,
    external_id text,
    channel_identifier_id bigint NOT NULL,
    source_id integer NOT NULL,
    update_date timestamp without time zone DEFAULT timezone('utc'::text, now()) NOT NULL,
    sync_date timestamp without time zone,
    update_user_id integer NOT NULL
);


--
-- Name: channel_identifier_source_channel_identifier_source_id_seq; Type: SEQUENCE; Schema: source; Owner: -
--

CREATE SEQUENCE channel_identifier_source_channel_identifier_source_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: channel_identifier_source_channel_identifier_source_id_seq; Type: SEQUENCE OWNED BY; Schema: source; Owner: -
--

ALTER SEQUENCE channel_identifier_source_channel_identifier_source_id_seq OWNED BY channel_identifier_source.channel_identifier_source_id;


--
-- Name: profile_source; Type: TABLE; Schema: source; Owner: -
--

CREATE TABLE profile_source (
    profile_source_id bigint NOT NULL,
    profile_id bigint NOT NULL,
    external_id text,
    source_id integer NOT NULL,
    update_date timestamp without time zone DEFAULT timezone('utc'::text, now()) NOT NULL,
    sync_date timestamp without time zone,
    update_user_id integer NOT NULL
);


--
-- Name: profile_source_profile_source_id_seq; Type: SEQUENCE; Schema: source; Owner: -
--

CREATE SEQUENCE profile_source_profile_source_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: profile_source_profile_source_id_seq; Type: SEQUENCE OWNED BY; Schema: source; Owner: -
--

ALTER SEQUENCE profile_source_profile_source_id_seq OWNED BY profile_source.profile_source_id;


--
-- Name: source; Type: TABLE; Schema: source; Owner: -
--

CREATE TABLE source (
    source_id integer NOT NULL,
    source_type_id integer NOT NULL,
    client_id integer,
    channel_id integer,
    name text,
    update_date timestamp without time zone NOT NULL,
    update_user_id integer NOT NULL
);


--
-- Name: TABLE source; Type: COMMENT; Schema: source; Owner: -
--

COMMENT ON TABLE source IS 'Shows all the sources that we have synced information from. Includes Client sources as well as channels such as ATT';


--
-- Name: source_source_id_seq; Type: SEQUENCE; Schema: source; Owner: -
--

CREATE SEQUENCE source_source_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: source_source_id_seq; Type: SEQUENCE OWNED BY; Schema: source; Owner: -
--

ALTER SEQUENCE source_source_id_seq OWNED BY source.source_id;


--
-- Name: source_type; Type: TABLE; Schema: source; Owner: -
--

CREATE TABLE source_type (
    source_type_id integer NOT NULL,
    name text NOT NULL
);


SET search_path = channel, pg_catalog;

--
-- Name: channel channel_id; Type: DEFAULT; Schema: channel; Owner: -
--

ALTER TABLE ONLY channel ALTER COLUMN channel_id SET DEFAULT nextval('channel_channel_id_seq'::regclass);


--
-- Name: channel_identifier channel_identifier_id; Type: DEFAULT; Schema: channel; Owner: -
--

ALTER TABLE ONLY channel_identifier ALTER COLUMN channel_identifier_id SET DEFAULT nextval('channel_identifier_channel_identifier_id_seq'::regclass);


SET search_path = "group", pg_catalog;

--
-- Name: group_filter_history history_id; Type: DEFAULT; Schema: group; Owner: -
--

ALTER TABLE ONLY group_filter_history ALTER COLUMN history_id SET DEFAULT nextval('group_filter_history_history_id_seq'::regclass);


--
-- Name: group_history history_id; Type: DEFAULT; Schema: group; Owner: -
--

ALTER TABLE ONLY group_history ALTER COLUMN history_id SET DEFAULT nextval('group_history_history_id_seq'::regclass);


--
-- Name: group_permission_history history_id; Type: DEFAULT; Schema: group; Owner: -
--

ALTER TABLE ONLY group_permission_history ALTER COLUMN history_id SET DEFAULT nextval('group_permission_history_history_id_seq'::regclass);


--
-- Name: group_profile group_profile_id; Type: DEFAULT; Schema: group; Owner: -
--

ALTER TABLE ONLY group_profile ALTER COLUMN group_profile_id SET DEFAULT nextval('group_profile_group_profile_id_seq'::regclass);


--
-- Name: group_profile_history history_id; Type: DEFAULT; Schema: group; Owner: -
--

ALTER TABLE ONLY group_profile_history ALTER COLUMN history_id SET DEFAULT nextval('group_profile_history_history_id_seq'::regclass);


--
-- Name: group_relationship group_relationship_id; Type: DEFAULT; Schema: group; Owner: -
--

ALTER TABLE ONLY group_relationship ALTER COLUMN group_relationship_id SET DEFAULT nextval('group_relationship_group_relationship_id_seq'::regclass);


--
-- Name: group_relationship_history history_id; Type: DEFAULT; Schema: group; Owner: -
--

ALTER TABLE ONLY group_relationship_history ALTER COLUMN history_id SET DEFAULT nextval('group_relationship_history_history_id_seq'::regclass);


--
-- Name: relationship_tree relationship_tree_id; Type: DEFAULT; Schema: group; Owner: -
--

ALTER TABLE ONLY relationship_tree ALTER COLUMN relationship_tree_id SET DEFAULT nextval('relationship_tree_relationship_tree_id_seq'::regclass);


SET search_path = profile, pg_catalog;

--
-- Name: channel_identifier_profile channel_identifier_profile_id; Type: DEFAULT; Schema: profile; Owner: -
--

ALTER TABLE ONLY channel_identifier_profile ALTER COLUMN channel_identifier_profile_id SET DEFAULT nextval('channel_identifier_profile_channel_identifier_profile_id_seq'::regclass);


--
-- Name: profile profile_id; Type: DEFAULT; Schema: profile; Owner: -
--

ALTER TABLE ONLY profile ALTER COLUMN profile_id SET DEFAULT nextval('profile_profile_id_seq'::regclass);


--
-- Name: profile_attribute profile_attribute_id; Type: DEFAULT; Schema: profile; Owner: -
--

ALTER TABLE ONLY profile_attribute ALTER COLUMN profile_attribute_id SET DEFAULT nextval('profile_attribute_profile_attribute_id_seq'::regclass);


--
-- Name: profile_attribute_value profile_attribute_value_id; Type: DEFAULT; Schema: profile; Owner: -
--

ALTER TABLE ONLY profile_attribute_value ALTER COLUMN profile_attribute_value_id SET DEFAULT nextval('profile_attribute_value_profile_attribute_value_id_seq'::regclass);


SET search_path = source, pg_catalog;

--
-- Name: channel_identifier_profile_source channel_identifier_profile_source_id; Type: DEFAULT; Schema: source; Owner: -
--

ALTER TABLE ONLY channel_identifier_profile_source ALTER COLUMN channel_identifier_profile_source_id SET DEFAULT nextval('channel_identifier_profile_so_channel_identifier_profile_so_seq'::regclass);


--
-- Name: channel_identifier_source channel_identifier_source_id; Type: DEFAULT; Schema: source; Owner: -
--

ALTER TABLE ONLY channel_identifier_source ALTER COLUMN channel_identifier_source_id SET DEFAULT nextval('channel_identifier_source_channel_identifier_source_id_seq'::regclass);


--
-- Name: profile_source profile_source_id; Type: DEFAULT; Schema: source; Owner: -
--

ALTER TABLE ONLY profile_source ALTER COLUMN profile_source_id SET DEFAULT nextval('profile_source_profile_source_id_seq'::regclass);


--
-- Name: source source_id; Type: DEFAULT; Schema: source; Owner: -
--

ALTER TABLE ONLY source ALTER COLUMN source_id SET DEFAULT nextval('source_source_id_seq'::regclass);


SET search_path = channel, pg_catalog;

--
-- Name: channel_identifier channel_identifier_channel_id_identifier_key; Type: CONSTRAINT; Schema: channel; Owner: -
--

ALTER TABLE ONLY channel_identifier
    ADD CONSTRAINT channel_identifier_channel_id_identifier_key UNIQUE (channel_id, identifier);


--
-- Name: channel_identifier channel_identifier_pkey; Type: CONSTRAINT; Schema: channel; Owner: -
--

ALTER TABLE ONLY channel_identifier
    ADD CONSTRAINT channel_identifier_pkey PRIMARY KEY (channel_identifier_id);


--
-- Name: channel_identifier_type channel_identifier_type_pkey; Type: CONSTRAINT; Schema: channel; Owner: -
--

ALTER TABLE ONLY channel_identifier_type
    ADD CONSTRAINT channel_identifier_type_pkey PRIMARY KEY (channel_identifier_type_id);


--
-- Name: channel channel_pkey; Type: CONSTRAINT; Schema: channel; Owner: -
--

ALTER TABLE ONLY channel
    ADD CONSTRAINT channel_pkey PRIMARY KEY (channel_id);


SET search_path = "group", pg_catalog;

--
-- Name: group_profile Key4; Type: CONSTRAINT; Schema: group; Owner: -
--

ALTER TABLE ONLY group_profile
    ADD CONSTRAINT "Key4" UNIQUE (profile_id, group_id);


--
-- Name: group_filter_history group_filter_history_pkey; Type: CONSTRAINT; Schema: group; Owner: -
--

ALTER TABLE ONLY group_filter_history
    ADD CONSTRAINT group_filter_history_pkey PRIMARY KEY (history_id);


--
-- Name: group_filter group_filter_pkey; Type: CONSTRAINT; Schema: group; Owner: -
--

ALTER TABLE ONLY group_filter
    ADD CONSTRAINT group_filter_pkey PRIMARY KEY (group_filter_id);


--
-- Name: group_history group_history_pkey; Type: CONSTRAINT; Schema: group; Owner: -
--

ALTER TABLE ONLY group_history
    ADD CONSTRAINT group_history_pkey PRIMARY KEY (history_id);


--
-- Name: group_permission_history group_permission_history_pkey; Type: CONSTRAINT; Schema: group; Owner: -
--

ALTER TABLE ONLY group_permission_history
    ADD CONSTRAINT group_permission_history_pkey PRIMARY KEY (history_id);


--
-- Name: group_permission group_permission_pkey; Type: CONSTRAINT; Schema: group; Owner: -
--

ALTER TABLE ONLY group_permission
    ADD CONSTRAINT group_permission_pkey PRIMARY KEY (group_permission_id);


--
-- Name: group group_pkey; Type: CONSTRAINT; Schema: group; Owner: -
--

ALTER TABLE ONLY "group"
    ADD CONSTRAINT group_pkey PRIMARY KEY (group_id);


--
-- Name: group_profile_history group_profile_history_pkey; Type: CONSTRAINT; Schema: group; Owner: -
--

ALTER TABLE ONLY group_profile_history
    ADD CONSTRAINT group_profile_history_pkey PRIMARY KEY (history_id);


--
-- Name: group_profile group_profile_pkey; Type: CONSTRAINT; Schema: group; Owner: -
--

ALTER TABLE ONLY group_profile
    ADD CONSTRAINT group_profile_pkey PRIMARY KEY (group_profile_id);


--
-- Name: group_relationship group_relationship_group_id_related_group_id_key; Type: CONSTRAINT; Schema: group; Owner: -
--

ALTER TABLE ONLY group_relationship
    ADD CONSTRAINT group_relationship_group_id_related_group_id_key UNIQUE (group_id, related_group_id);


--
-- Name: group_relationship_history group_relationship_history_pkey; Type: CONSTRAINT; Schema: group; Owner: -
--

ALTER TABLE ONLY group_relationship_history
    ADD CONSTRAINT group_relationship_history_pkey PRIMARY KEY (history_id);


--
-- Name: group_relationship group_relationship_pkey; Type: CONSTRAINT; Schema: group; Owner: -
--

ALTER TABLE ONLY group_relationship
    ADD CONSTRAINT group_relationship_pkey PRIMARY KEY (group_relationship_id);


--
-- Name: group_type group_type_pkey; Type: CONSTRAINT; Schema: group; Owner: -
--

ALTER TABLE ONLY group_type
    ADD CONSTRAINT group_type_pkey PRIMARY KEY (group_type_id);


--
-- Name: relationship_tree relationship_tree_path_key; Type: CONSTRAINT; Schema: group; Owner: -
--

ALTER TABLE ONLY relationship_tree
    ADD CONSTRAINT relationship_tree_path_key UNIQUE (path);


--
-- Name: relationship_tree relationship_tree_pkey; Type: CONSTRAINT; Schema: group; Owner: -
--

ALTER TABLE ONLY relationship_tree
    ADD CONSTRAINT relationship_tree_pkey PRIMARY KEY (relationship_tree_id);


--
-- Name: relationship_type relationship_type_pkey; Type: CONSTRAINT; Schema: group; Owner: -
--

ALTER TABLE ONLY relationship_type
    ADD CONSTRAINT relationship_type_pkey PRIMARY KEY (relationship_type_id);


SET search_path = profile, pg_catalog;

--
-- Name: channel_identifier_profile channel_identifier_profile_pkey; Type: CONSTRAINT; Schema: profile; Owner: -
--

ALTER TABLE ONLY channel_identifier_profile
    ADD CONSTRAINT channel_identifier_profile_pkey PRIMARY KEY (channel_identifier_profile_id);


--
-- Name: profile_attribute profile_attribute_pkey; Type: CONSTRAINT; Schema: profile; Owner: -
--

ALTER TABLE ONLY profile_attribute
    ADD CONSTRAINT profile_attribute_pkey PRIMARY KEY (profile_attribute_id);


--
-- Name: profile_attribute_value profile_attribute_value_pkey; Type: CONSTRAINT; Schema: profile; Owner: -
--

ALTER TABLE ONLY profile_attribute_value
    ADD CONSTRAINT profile_attribute_value_pkey PRIMARY KEY (profile_attribute_value_id);


--
-- Name: profile_attribute_value profile_attribute_value_profile_id_attribute_id_key; Type: CONSTRAINT; Schema: profile; Owner: -
--

ALTER TABLE ONLY profile_attribute_value
    ADD CONSTRAINT profile_attribute_value_profile_id_attribute_id_key UNIQUE (profile_id, profile_attribute_id);


--
-- Name: profile profile_client_id_user_id_key; Type: CONSTRAINT; Schema: profile; Owner: -
--

ALTER TABLE ONLY profile
    ADD CONSTRAINT profile_client_id_user_id_key UNIQUE (client_id, user_id);


--
-- Name: profile profile_pkey; Type: CONSTRAINT; Schema: profile; Owner: -
--

ALTER TABLE ONLY profile
    ADD CONSTRAINT profile_pkey PRIMARY KEY (profile_id);


--
-- Name: profile_type profile_type_pkey; Type: CONSTRAINT; Schema: profile; Owner: -
--

ALTER TABLE ONLY profile_type
    ADD CONSTRAINT profile_type_pkey PRIMARY KEY (profile_type_id);


SET search_path = source, pg_catalog;

--
-- Name: channel_identifier_source channel_identifer_source_pkey; Type: CONSTRAINT; Schema: source; Owner: -
--

ALTER TABLE ONLY channel_identifier_source
    ADD CONSTRAINT channel_identifer_source_pkey PRIMARY KEY (channel_identifier_source_id);


--
-- Name: channel_identifier_profile_source channel_identifier_profile_source_pkey; Type: CONSTRAINT; Schema: source; Owner: -
--

ALTER TABLE ONLY channel_identifier_profile_source
    ADD CONSTRAINT channel_identifier_profile_source_pkey PRIMARY KEY (channel_identifier_profile_source_id);


--
-- Name: profile_source profile_source_pkey; Type: CONSTRAINT; Schema: source; Owner: -
--

ALTER TABLE ONLY profile_source
    ADD CONSTRAINT profile_source_pkey PRIMARY KEY (profile_source_id);


--
-- Name: source source_pkey; Type: CONSTRAINT; Schema: source; Owner: -
--

ALTER TABLE ONLY source
    ADD CONSTRAINT source_pkey PRIMARY KEY (source_id);


--
-- Name: source_type source_type_pkey; Type: CONSTRAINT; Schema: source; Owner: -
--

ALTER TABLE ONLY source_type
    ADD CONSTRAINT source_type_pkey PRIMARY KEY (source_type_id);


SET search_path = channel, pg_catalog;

--
-- Name: channel_identifier_channel_id_idx; Type: INDEX; Schema: channel; Owner: -
--

CREATE INDEX channel_identifier_channel_id_idx ON channel_identifier USING btree (channel_id);


SET search_path = "group", pg_catalog;

--
-- Name: group_client_id_external_id_key; Type: INDEX; Schema: group; Owner: -
--

CREATE UNIQUE INDEX group_client_id_external_id_key ON "group" USING btree (client_id, external_id);


--
-- Name: group_client_id_parent_group_id_name_key; Type: INDEX; Schema: group; Owner: -
--

CREATE UNIQUE INDEX group_client_id_parent_group_id_name_key ON "group" USING btree (client_id, parent_group_id, name) WHERE (parent_group_id IS NOT NULL);


--
-- Name: group_permission_group_id_user_id_key; Type: INDEX; Schema: group; Owner: -
--

CREATE UNIQUE INDEX group_permission_group_id_user_id_key ON group_permission USING btree (group_id, user_id);


--
-- Name: group_permission_user_id_group_id_key; Type: INDEX; Schema: group; Owner: -
--

CREATE UNIQUE INDEX group_permission_user_id_group_id_key ON group_permission USING btree (user_id, group_id);


--
-- Name: relationship_tree_path_idx; Type: INDEX; Schema: group; Owner: -
--

CREATE INDEX relationship_tree_path_idx ON relationship_tree USING gist (path);


SET search_path = profile, pg_catalog;

--
-- Name: channel_identifier_profile_channel_identifier_id_idx; Type: INDEX; Schema: profile; Owner: -
--

CREATE INDEX channel_identifier_profile_channel_identifier_id_idx ON channel_identifier_profile USING btree (channel_identifier_id);


--
-- Name: channel_identifier_profile_effective_dates_idx; Type: INDEX; Schema: profile; Owner: -
--

CREATE INDEX channel_identifier_profile_effective_dates_idx ON channel_identifier_profile USING gist (tsrange(effective_from, effective_to));


--
-- Name: channel_identifier_profile_profile_id_idx; Type: INDEX; Schema: profile; Owner: -
--

CREATE INDEX channel_identifier_profile_profile_id_idx ON channel_identifier_profile USING btree (profile_id);


--
-- Name: profile_attribute_client_id_name_key; Type: INDEX; Schema: profile; Owner: -
--

CREATE UNIQUE INDEX profile_attribute_client_id_name_key ON profile_attribute USING btree (client_id, name);


--
-- Name: profile_attribute_value_profile_attribute_id_idx; Type: INDEX; Schema: profile; Owner: -
--

CREATE INDEX profile_attribute_value_profile_attribute_id_idx ON profile_attribute_value USING btree (profile_attribute_id);


--
-- Name: profile_attribute_value_profile_id_idx; Type: INDEX; Schema: profile; Owner: -
--

CREATE INDEX profile_attribute_value_profile_id_idx ON profile_attribute_value USING btree (profile_id);


--
-- Name: profile_profile_type_id_idx; Type: INDEX; Schema: profile; Owner: -
--

CREATE INDEX profile_profile_type_id_idx ON profile USING btree (profile_type_id);


SET search_path = source, pg_catalog;

--
-- Name: channel_identifier_channel_identifier_id_idx; Type: INDEX; Schema: source; Owner: -
--

CREATE INDEX channel_identifier_channel_identifier_id_idx ON channel_identifier_source USING btree (channel_identifier_id);


--
-- Name: channel_identifier_profile_source_channel_identifier_profile_id; Type: INDEX; Schema: source; Owner: -
--

CREATE INDEX channel_identifier_profile_source_channel_identifier_profile_id ON channel_identifier_profile_source USING btree (channel_identifier_profile_id);


--
-- Name: channel_identifier_profile_source_source_id_idx; Type: INDEX; Schema: source; Owner: -
--

CREATE INDEX channel_identifier_profile_source_source_id_idx ON channel_identifier_profile_source USING btree (source_id);


--
-- Name: channel_identifier_source_source_id_idx; Type: INDEX; Schema: source; Owner: -
--

CREATE INDEX channel_identifier_source_source_id_idx ON channel_identifier_source USING btree (source_id);


--
-- Name: profile_source_profile_id_idx; Type: INDEX; Schema: source; Owner: -
--

CREATE INDEX profile_source_profile_id_idx ON profile_source USING btree (profile_id);


--
-- Name: profile_source_source_id_idx; Type: INDEX; Schema: source; Owner: -
--

CREATE INDEX profile_source_source_id_idx ON profile_source USING btree (source_id);


--
-- Name: source_source_type_id_idx; Type: INDEX; Schema: source; Owner: -
--

CREATE INDEX source_source_type_id_idx ON source USING btree (source_type_id);


SET search_path = "group", pg_catalog;

--
-- Name: group_filter tr_group_filter_insert_history; Type: TRIGGER; Schema: group; Owner: -
--

CREATE TRIGGER tr_group_filter_insert_history AFTER INSERT OR DELETE OR UPDATE ON group_filter FOR EACH ROW EXECUTE PROCEDURE trf_group_filter_insert_history();


--
-- Name: group tr_group_insert_history; Type: TRIGGER; Schema: group; Owner: -
--

CREATE TRIGGER tr_group_insert_history AFTER INSERT OR DELETE OR UPDATE ON "group" FOR EACH ROW EXECUTE PROCEDURE trf_group_insert_history();


--
-- Name: group_permission tr_group_permission_insert_history; Type: TRIGGER; Schema: group; Owner: -
--

CREATE TRIGGER tr_group_permission_insert_history AFTER INSERT OR DELETE OR UPDATE ON group_permission FOR EACH ROW EXECUTE PROCEDURE trf_group_permission_insert_history();


--
-- Name: group_profile tr_group_profile_insert_history; Type: TRIGGER; Schema: group; Owner: -
--

CREATE TRIGGER tr_group_profile_insert_history AFTER INSERT OR DELETE OR UPDATE ON group_profile FOR EACH ROW EXECUTE PROCEDURE trf_group_profile_insert_history();


--
-- Name: group_relationship tr_group_relationship_insert_history; Type: TRIGGER; Schema: group; Owner: -
--

CREATE TRIGGER tr_group_relationship_insert_history AFTER INSERT OR DELETE OR UPDATE ON group_relationship FOR EACH ROW EXECUTE PROCEDURE trf_group_relationship_insert_history();


SET search_path = profile, pg_catalog;

--
-- Name: channel_identifier_profile tr_channel_identifier_profile; Type: TRIGGER; Schema: profile; Owner: -
--

CREATE CONSTRAINT TRIGGER tr_channel_identifier_profile AFTER INSERT OR UPDATE ON channel_identifier_profile DEFERRABLE INITIALLY DEFERRED FOR EACH ROW EXECUTE PROCEDURE trf_channel_identifier_profile();


SET search_path = channel, pg_catalog;

--
-- Name: channel_identifier channel_identifier_channel_id_fkey; Type: FK CONSTRAINT; Schema: channel; Owner: -
--

ALTER TABLE ONLY channel_identifier
    ADD CONSTRAINT channel_identifier_channel_id_fkey FOREIGN KEY (channel_id) REFERENCES channel(channel_id);


SET search_path = "group", pg_catalog;

--
-- Name: group_filter group_filter_group_id_fkey; Type: FK CONSTRAINT; Schema: group; Owner: -
--

ALTER TABLE ONLY group_filter
    ADD CONSTRAINT group_filter_group_id_fkey FOREIGN KEY (group_id) REFERENCES "group"(group_id);


--
-- Name: group group_group_type_id_fkey; Type: FK CONSTRAINT; Schema: group; Owner: -
--

ALTER TABLE ONLY "group"
    ADD CONSTRAINT group_group_type_id_fkey FOREIGN KEY (group_type_id) REFERENCES group_type(group_type_id);


--
-- Name: group group_parent_group_id_fkey; Type: FK CONSTRAINT; Schema: group; Owner: -
--

ALTER TABLE ONLY "group"
    ADD CONSTRAINT group_parent_group_id_fkey FOREIGN KEY (parent_group_id) REFERENCES "group"(group_id);


--
-- Name: group_permission group_permission_group_id_fkey; Type: FK CONSTRAINT; Schema: group; Owner: -
--

ALTER TABLE ONLY group_permission
    ADD CONSTRAINT group_permission_group_id_fkey FOREIGN KEY (group_id) REFERENCES "group"(group_id);


--
-- Name: group_profile group_profile_group_id_fkey; Type: FK CONSTRAINT; Schema: group; Owner: -
--

ALTER TABLE ONLY group_profile
    ADD CONSTRAINT group_profile_group_id_fkey FOREIGN KEY (group_id) REFERENCES "group"(group_id);


--
-- Name: group_profile group_profile_profile_id_fkey; Type: FK CONSTRAINT; Schema: group; Owner: -
--

ALTER TABLE ONLY group_profile
    ADD CONSTRAINT group_profile_profile_id_fkey FOREIGN KEY (profile_id) REFERENCES profile.profile(profile_id);


--
-- Name: group_relationship group_relationship_group_id_fkey; Type: FK CONSTRAINT; Schema: group; Owner: -
--

ALTER TABLE ONLY group_relationship
    ADD CONSTRAINT group_relationship_group_id_fkey FOREIGN KEY (group_id) REFERENCES "group"(group_id);


--
-- Name: group_relationship group_relationship_related_group_id_fkey; Type: FK CONSTRAINT; Schema: group; Owner: -
--

ALTER TABLE ONLY group_relationship
    ADD CONSTRAINT group_relationship_related_group_id_fkey FOREIGN KEY (related_group_id) REFERENCES "group"(group_id);


--
-- Name: group_relationship group_relationship_relationship_type_id_fkey; Type: FK CONSTRAINT; Schema: group; Owner: -
--

ALTER TABLE ONLY group_relationship
    ADD CONSTRAINT group_relationship_relationship_type_id_fkey FOREIGN KEY (relationship_type_id) REFERENCES relationship_type(relationship_type_id);


--
-- Name: group group_source_id_fkey; Type: FK CONSTRAINT; Schema: group; Owner: -
--

ALTER TABLE ONLY "group"
    ADD CONSTRAINT group_source_id_fkey FOREIGN KEY (source_id) REFERENCES source.source(source_id);


--
-- Name: relationship_tree relationship_tree_group_relationship_id_fkey; Type: FK CONSTRAINT; Schema: group; Owner: -
--

ALTER TABLE ONLY relationship_tree
    ADD CONSTRAINT relationship_tree_group_relationship_id_fkey FOREIGN KEY (group_relationship_id) REFERENCES group_relationship(group_relationship_id);


SET search_path = profile, pg_catalog;

--
-- Name: channel_identifier_profile channel_identifier_profile_channel_identifier_id_fkey; Type: FK CONSTRAINT; Schema: profile; Owner: -
--

ALTER TABLE ONLY channel_identifier_profile
    ADD CONSTRAINT channel_identifier_profile_channel_identifier_id_fkey FOREIGN KEY (channel_identifier_id) REFERENCES channel.channel_identifier(channel_identifier_id);


--
-- Name: channel_identifier_profile channel_identifier_profile_profile_id_fkey; Type: FK CONSTRAINT; Schema: profile; Owner: -
--

ALTER TABLE ONLY channel_identifier_profile
    ADD CONSTRAINT channel_identifier_profile_profile_id_fkey FOREIGN KEY (profile_id) REFERENCES profile(profile_id);


--
-- Name: profile_attribute_value profile_attribute_value_profile_attribute_id_fkey; Type: FK CONSTRAINT; Schema: profile; Owner: -
--

ALTER TABLE ONLY profile_attribute_value
    ADD CONSTRAINT profile_attribute_value_profile_attribute_id_fkey FOREIGN KEY (profile_attribute_id) REFERENCES profile_attribute(profile_attribute_id);


--
-- Name: profile_attribute_value profile_attribute_value_profile_id_fkey; Type: FK CONSTRAINT; Schema: profile; Owner: -
--

ALTER TABLE ONLY profile_attribute_value
    ADD CONSTRAINT profile_attribute_value_profile_id_fkey FOREIGN KEY (profile_id) REFERENCES profile(profile_id);


--
-- Name: profile profile_profile_type_id_fkey; Type: FK CONSTRAINT; Schema: profile; Owner: -
--

ALTER TABLE ONLY profile
    ADD CONSTRAINT profile_profile_type_id_fkey FOREIGN KEY (profile_type_id) REFERENCES profile_type(profile_type_id);


SET search_path = source, pg_catalog;

--
-- Name: channel_identifier_source channel_identifier_channel_identifier_id_fkey; Type: FK CONSTRAINT; Schema: source; Owner: -
--

ALTER TABLE ONLY channel_identifier_source
    ADD CONSTRAINT channel_identifier_channel_identifier_id_fkey FOREIGN KEY (channel_identifier_id) REFERENCES channel.channel_identifier(channel_identifier_id);


--
-- Name: channel_identifier_profile_source channel_identifier_profile_source_channel_identifier_profile_fk; Type: FK CONSTRAINT; Schema: source; Owner: -
--

ALTER TABLE ONLY channel_identifier_profile_source
    ADD CONSTRAINT channel_identifier_profile_source_channel_identifier_profile_fk FOREIGN KEY (channel_identifier_profile_id) REFERENCES profile.channel_identifier_profile(channel_identifier_profile_id);


--
-- Name: channel_identifier_profile_source channel_identifier_profile_source_source_id_fkey; Type: FK CONSTRAINT; Schema: source; Owner: -
--

ALTER TABLE ONLY channel_identifier_profile_source
    ADD CONSTRAINT channel_identifier_profile_source_source_id_fkey FOREIGN KEY (source_id) REFERENCES source(source_id);


--
-- Name: channel_identifier_source channel_identifier_source_source_id_fkey; Type: FK CONSTRAINT; Schema: source; Owner: -
--

ALTER TABLE ONLY channel_identifier_source
    ADD CONSTRAINT channel_identifier_source_source_id_fkey FOREIGN KEY (source_id) REFERENCES source(source_id);


--
-- Name: profile_source profile_source_profile_id_fkey; Type: FK CONSTRAINT; Schema: source; Owner: -
--

ALTER TABLE ONLY profile_source
    ADD CONSTRAINT profile_source_profile_id_fkey FOREIGN KEY (profile_id) REFERENCES profile.profile(profile_id);


--
-- Name: profile_source profile_source_source_id_fkey; Type: FK CONSTRAINT; Schema: source; Owner: -
--

ALTER TABLE ONLY profile_source
    ADD CONSTRAINT profile_source_source_id_fkey FOREIGN KEY (source_id) REFERENCES source(source_id);


--
-- Name: source source_source_type_id_fkey; Type: FK CONSTRAINT; Schema: source; Owner: -
--

ALTER TABLE ONLY source
    ADD CONSTRAINT source_source_type_id_fkey FOREIGN KEY (source_type_id) REFERENCES source_type(source_type_id);


--
-- PostgreSQL database dump complete
--