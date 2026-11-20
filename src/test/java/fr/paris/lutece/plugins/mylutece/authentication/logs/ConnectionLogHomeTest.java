/*
 * Copyright (c) 2002-2026, City of Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.mylutece.authentication.logs;

import java.security.SecureRandom;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.plugins.mylutece.service.MyLutecePlugin;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.test.LuteceTestCase;

/**
 * Count of the failed front office connections
 */
public class ConnectionLogHomeTest extends LuteceTestCase
{
    /**
     * The failures are counted per login, whatever the address, and per address, whatever the login
     */
    @Test
    public void testLoginErrorsPerLoginAndPerAddress( )
    {
        Plugin plugin = PluginService.getPlugin( MyLutecePlugin.PLUGIN_NAME );
        String strLogin = "junit_" + Math.abs( new SecureRandom( ).nextLong( ) );
        String strBase = "10." + new SecureRandom( ).nextInt( 200 ) + "." + new SecureRandom( ).nextInt( 200 ) + ".";

        for ( int i = 1; i <= 3; i++ )
        {
            ConnectionLog log = new ConnectionLog( );
            log.setIpAddress( strBase + i );
            log.setLogin( strLogin );
            log.setDateLogin( new java.sql.Timestamp( System.currentTimeMillis( ) - 60000L ) );
            log.setLoginStatus( ConnectionLog.LOGIN_DENIED );
            ConnectionLogHome.addUserLog( log, plugin );
        }

        ConnectionLog query = new ConnectionLog( );
        query.setLogin( strLogin );
        query.setIpAddress( strBase + "1" );

        assertEquals( 3, ConnectionLogHome.getLoginErrorsByLogin( query, 10, plugin ) );
        assertEquals( 1, ConnectionLogHome.getLoginErrors( query, 10, plugin ) );

        query.setLogin( strLogin + "_other" );
        assertEquals( 0, ConnectionLogHome.getLoginErrorsByLogin( query, 10, plugin ) );
    }
}
